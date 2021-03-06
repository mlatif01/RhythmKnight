package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.latif.rhythmknight.Sprites.RKnight;

public class GameOver implements Screen {
  private Viewport viewport;
  private Stage stage;

  private Game game;

  public GameOver(Game game, int finalScore) {
    this.game = game;
    viewport = new FitViewport(RhythmKnight.V_WIDTH, RhythmKnight.V_HEIGHT,
            new OrthographicCamera());
    stage = new Stage(viewport, ((RhythmKnight) game).batch);

    Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    Table table = new Table();
    table.center();
    table.setFillParent(true);

    Label gameOverLabel = new Label("GAME OVER", font);
    Label playAgainLabel = new Label("Tap to Play Again", font);
    Label StageOverLabel = new Label("STAGE OVER", font);
    Label WinLabel = new Label("Stage Completed! - Tap for Next Stage", font);
    Label finalScoreLabel = new Label("Final Score: " + finalScore, font);

    stage.addActor(table);

    // win
    // TODO
    if (RKnight.getHp() != 0) {
      table.add(StageOverLabel).expandX();
      table.row();
      table.add(WinLabel).expandX().padTop(10f);
      table.row();
      table.row();
      table.add(finalScoreLabel);
      RhythmKnight.manager.get("audio/music/gamewin2.wav", Music.class).play();
      RKnight.incrementExp(50);
    }
    // lose
    else {
      table.add(gameOverLabel).expandX();
      table.row();
      table.add(playAgainLabel).expandX().padTop(10f);
      table.row();
      table.row();
      table.add(finalScoreLabel);
      RhythmKnight.manager.get("audio/music/gameover2.wav", Music.class).play();
    }
  }


  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {

    // restart game when screen is touched
    if (Gdx.input.isTouched()) {
      game.setScreen(new StageSelect((RhythmKnight) game));
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
    RhythmKnight.manager.get("audio/music/gamewin.ogg", Music.class).stop();
    RhythmKnight.manager.get("audio/music/gamewin2.wav", Music.class).stop();
    RhythmKnight.manager.get("audio/music/gameover.ogg", Music.class).stop();
    RhythmKnight.manager.get("audio/music/gameover2.wav", Music.class).stop();
    RhythmKnight.manager.get("audio/sounds/gamerestart.wav", Sound.class).play();
    stage.dispose();
  }

}
