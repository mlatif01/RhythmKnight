package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Sprites.RKnight;

import java.io.File;
import java.io.FileNotFoundException;

import featherdev.lwbd.decoders.JLayerMp3Decoder;
import featherdev.lwbd.decoders.LwbdDecoder;

public class StageSelect implements Screen {

  private Viewport viewport;
  private Stage stage;
  // a camera with orthographic projection
  private OrthographicCamera gameCam;

  private Game game;

  private Texture txtrLevel1_1;
  private Texture txtrLevel1_2;
  private Texture txtrLevel1_3;
  private Texture txtrBackground;

  private Sprite spriteBackground;
  private SpriteBatch batch;

  private boolean changeScreen = false;
  private static boolean useBackground1 = true;
  private String texturePath;

  public StageSelect(Game game) {
    this.game = game;

    batch = new SpriteBatch();
    gameCam = new OrthographicCamera();
    viewport = new FitViewport(RhythmKnight.V_WIDTH,
            RhythmKnight.V_HEIGHT, gameCam);

    stage = new Stage(viewport, ((RhythmKnight) game).batch);

    txtrLevel1_1 = new Texture(Gdx.files.internal("btn_level_1.png"));
    txtrLevel1_2 = new Texture(Gdx.files.internal("btn_level_1_2.png"));
    txtrLevel1_3 = new Texture(Gdx.files.internal("btn_level_1_3.png"));
    txtrBackground = new Texture(Gdx.files.internal("country-platform-back.png"));

    spriteBackground = new Sprite(txtrBackground);

    Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    Table table = new Table();
    table.center();
    table.setFillParent(true);

    // alternate stage select background
    if (useBackground1) {
      texturePath = "country-platform-back.png";
      useBackground1 = false;
    }
    else if (!useBackground1) {
      texturePath = "pixel_background.png";
      useBackground1 = true;
    }

    // set background
    table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(texturePath))));
    ImageButton btnLevel1_1 = new ImageButton(
            new TextureRegionDrawable(
                    new TextureRegion(txtrLevel1_1)));

    // create buttons
    btnLevel1_1.setPosition(50.f, 10.f, Align.bottomLeft);
    btnLevel1_1.setSize(80, 80);
    table.addActor(btnLevel1_1);

    ImageButton btnLevel1_2 = new ImageButton(
            new TextureRegionDrawable(
                    new TextureRegion(txtrLevel1_2)));
    btnLevel1_2.setPosition(150.f, 10.f, Align.bottomLeft);
    btnLevel1_2.setSize(80, 80);
    table.addActor(btnLevel1_2);

    ImageButton btnLevel1_3 = new ImageButton(
            new TextureRegionDrawable(
                    new TextureRegion(txtrLevel1_3)));
    btnLevel1_3.setPosition(250.f, 10.f, Align.bottomLeft);
    btnLevel1_3.setSize(80, 80);
    table.addActor(btnLevel1_3);


    Label playLabel = new Label("STAGE SELECT", font);
    table.add(playLabel).expandX();
    table.row();
    Label statsLabel = new Label("RK STATS", font);
    statsLabel.setFontScale(0.8f);
    table.add(statsLabel).padRight(300f);
    table.row();
    Label hpLabel = new Label("HP: " + 30, font);
    hpLabel.setFontScale(0.7f);
    table.add(hpLabel).padRight(300f);
    table.row();
    Label lvlUpLabel = new Label("LVL UP: " + RKnight.getExpToNextLevel(), font);
    lvlUpLabel.setFontScale(0.7f);
    table.add(lvlUpLabel).padRight(300f);
    table.row();
    Label currentLvl = new Label("LVL: " + RKnight.getCurrentLvl(), font);
    currentLvl.setFontScale(0.7f);
    table.add(currentLvl).padBottom(80f).padRight(300f);

    // add actors to table
    stage.addActor(table);

    Gdx.input.setInputProcessor(stage); //Start taking input from the ui

    // event listener for tapping on stage
    btnLevel1_1.addListener(new EventListener() {
      @Override
      public boolean handle(Event event) {
        if (Gdx.input.isTouched()) {
          changeScreen = true;
        }
        return false;
      }
    });

  }


  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    // clear screen and set projection matrix
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.setProjectionMatrix(gameCam.combined);

    // draw stage components
    stage.draw();

    // change screen when user taps on button
    if (changeScreen) {
      RhythmKnight.manager.get("audio/sounds/gamerestart.wav", Sound.class).play();
      game.setScreen(new LoadScreen((RhythmKnight) game));
    }

  }

  @Override
  public void resize(int width, int height) {
    // important that the viewport is adjusted so that it knows what the actual screen size is
    viewport.update(width, height);
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    stage.dispose();
  }
}
