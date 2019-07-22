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

  private Game game;

  private Texture txtrLevel1_1;
  private Texture txtrLevel1_2;

  private boolean changeScreen = false;

  public StageSelect(Game game) {
    this.game = game;
    viewport = new StretchViewport(RhythmKnight.V_WIDTH, RhythmKnight.V_HEIGHT,
            new OrthographicCamera());
    stage = new Stage(viewport, ((RhythmKnight) game).batch);

    txtrLevel1_1 = new Texture(Gdx.files.internal("btn_level_1.png") );
    txtrLevel1_2 = new Texture(Gdx.files.internal("btn_level_1_2.png") );

    Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    Table table = new Table();
    table.center();
    table.setFillParent(true);

    ImageButton btnLevel1_1 = new ImageButton(
            new TextureRegionDrawable(
                    new TextureRegion(txtrLevel1_1)));
    btnLevel1_1.setPosition(50.f, 50.f, Align.bottomLeft);
    btnLevel1_1.setSize(50, 50);
    table.addActor(btnLevel1_1);

    ImageButton btnLevel1_2 = new ImageButton(
            new TextureRegionDrawable(
                    new TextureRegion(txtrLevel1_2)));
    btnLevel1_2.setPosition(120.f, 50.f, Align.bottomLeft);
    btnLevel1_2.setSize(50, 50);
    table.addActor(btnLevel1_2);


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
    if (changeScreen) {
      RhythmKnight.manager.get("audio/sounds/gamerestart.wav", Sound.class).play();
      game.setScreen(new PlayScreen((RhythmKnight) game));
    }

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    stage.draw();
  }

  @Override
  public void resize(int width, int height) {

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
