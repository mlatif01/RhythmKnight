package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latif.rhythmknight.RhythmKnight;

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

  public StageSelect(Game game) {
    this.game = game;

    batch = new SpriteBatch();
    gameCam = new OrthographicCamera();
    viewport = new FitViewport(RhythmKnight.V_WIDTH,
            RhythmKnight.V_HEIGHT, gameCam);

    stage = new Stage(viewport, ((RhythmKnight) game).batch);

    txtrLevel1_1 = new Texture(Gdx.files.internal("btn_level_1.png") );
    txtrLevel1_2 = new Texture(Gdx.files.internal("btn_level_1_2.png") );
    txtrLevel1_3 = new Texture(Gdx.files.internal("btn_level_1_3.png") );
    txtrBackground = new Texture(Gdx.files.internal("country-platform-back.png"));

    spriteBackground = new Sprite(txtrBackground);

    Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    Table table = new Table();
    table.center();
    table.setFillParent(true);
    table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("country-platform-back.png"))));
    ImageButton btnLevel1_1 = new ImageButton(
            new TextureRegionDrawable(
                    new TextureRegion(txtrLevel1_1)));

    btnLevel1_1.setPosition(50.f, 50.f, Align.bottomLeft);
    btnLevel1_1.setSize(70, 70);
    table.addActor(btnLevel1_1);

    ImageButton btnLevel1_2 = new ImageButton(
            new TextureRegionDrawable(
                    new TextureRegion(txtrLevel1_2)));
    btnLevel1_2.setPosition(150.f, 50.f, Align.bottomLeft);
    btnLevel1_2.setSize(70, 70);
    table.addActor(btnLevel1_2);

    ImageButton btnLevel1_3 = new ImageButton(
            new TextureRegionDrawable(
                    new TextureRegion(txtrLevel1_3)));
    btnLevel1_3.setPosition(250.f, 50.f, Align.bottomLeft);
    btnLevel1_3.setSize(70, 70);
    table.addActor(btnLevel1_3);


    Label playLabel = new Label("Stage Select", font);
    table.add(playLabel).expandX().padBottom(150f);
    stage.addActor(table);

    Gdx.input.setInputProcessor(stage); //Start taking input from the ui

    btnLevel1_1.addListener(new EventListener()
    {
      @Override
      public boolean handle(Event event)
      {
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
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.setProjectionMatrix(gameCam.combined);

    stage.draw();

    if (changeScreen) {
      RhythmKnight.manager.get("audio/sounds/gamerestart.wav", Sound.class).play();
      game.setScreen(new PlayScreen((RhythmKnight) game));
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
