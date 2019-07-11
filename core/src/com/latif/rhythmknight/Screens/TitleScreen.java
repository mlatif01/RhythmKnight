package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Tools.BeatDetector;

public class TitleScreen implements Screen {

  private Viewport viewport;
  private Stage stage;

  private Game game;

  public TitleScreen(Game game) {
    this.game = game;
    viewport = new FitViewport(RhythmKnight.V_WIDTH, RhythmKnight.V_HEIGHT,
            new OrthographicCamera());
    stage = new Stage(viewport, ((RhythmKnight) game).batch);

    Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    Table table = new Table();
    table.center();
    table.setFillParent(true);

    Label rhythmKnightLabel = new Label("RHYTHM KNIGHT", font);
    Label pressStart = new Label("PRESS START", font);
    stage.addActor(table);

    table.add(rhythmKnightLabel).expandX();
    table.row();
    table.add(pressStart).expandX().padTop(10f);

  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {

    // Start game when screen is touched
    if (Gdx.input.isTouched()) {
      game.setScreen(new PlayScreen((RhythmKnight) game));
      dispose();
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
