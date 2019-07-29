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
import com.latif.rhythmknight.Sprites.Gobling;
import com.latif.rhythmknight.Sprites.RKnight;
import com.latif.rhythmknight.Tools.LWBDBeatDetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;

import featherdev.lwbd.Beat;
import featherdev.lwbd.decoders.JLayerMp3Decoder;
import featherdev.lwbd.decoders.LwbdDecoder;

/*
Loading screen for processing using LWBD
 */

public class LoadScreen implements Screen {
  private Viewport viewport;
  private Stage stage;

  private Game game;

  private boolean processed;
  private boolean processing;
  private float screenTimer;

  LWBDBeatDetector lwbd;

  public LoadScreen(Game game) {
    this.game = game;

    processed = false;
    processing = false;

    screenTimer = 0f;

    viewport = new FitViewport(RhythmKnight.V_WIDTH, RhythmKnight.V_HEIGHT,
            new OrthographicCamera());
    stage = new Stage(viewport, ((RhythmKnight) game).batch);

    Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    Table table = new Table();
    table.center();
    table.setFillParent(true);

    Label loadingLabel = new Label("Loading...", font);

    stage.addActor(table);

    // TODO
    table.add(loadingLabel);
  }

  public void setupLWBD() {
    processing = true;
    // Specify song file name here
    lwbd = new LWBDBeatDetector("audio/song1.mp3");
    processed = true;
  }


  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {

    if (!processing && screenTimer > 2f) {
      setupLWBD();
      screenTimer = 0f;
    }

    screenTimer += delta;

    // when audio is processed start game
    if (processed && screenTimer > 2f) {
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
