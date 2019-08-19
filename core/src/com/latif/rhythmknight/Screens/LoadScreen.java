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
import com.latif.rhythmknight.Tools.LWBDBeatDetector;
import com.latif.rhythmknight.Tools.TarsosPitchDetector;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

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

  private LWBDBeatDetector lwbd;
  private TarsosPitchDetector tarsos;

  private int level;

  public LoadScreen(Game game, int level) {
    this.game = game;
    this.level = level;

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

  public void setupLWBD() throws IOException, ClassNotFoundException {
    processing = true;
    // Specify mp3 song file name here with beat detection library to use
    // TODO: Improve this to allow for loading multiple songs
    if (level == 1) {
      lwbd = new LWBDBeatDetector(RhythmKnight.STAGE_ONE_MP3, level);
    } else if (level == 2){
      lwbd = new LWBDBeatDetector(RhythmKnight.STAGE_ONE_ALTERNATE_MP3, level);
    } else if (level == 3) {
      lwbd = new LWBDBeatDetector(RhythmKnight.STAGE_THREE_MP3, level);
    }
    processed = true;
  }

  public void setupLWBDWBD() throws IOException, ClassNotFoundException {
    processing = true;
    // Specify mp3 song file name here with beat detection library to use
    // TODO: Improve this to allow for loading multiple songs
    if (level == 1) {
      LWBDBeatDetector.readSong1();
    } else if (level == 2){
      LWBDBeatDetector.readSong2();
    } else if (level == 3) {
      LWBDBeatDetector.readSong3();
    }
    processed = true;
  }

  public void setupTarsos() throws IOException, UnsupportedAudioFileException {
    processing = true;
    // Specify song file name here with beat detection library to use
    tarsos = new TarsosPitchDetector();
    processed = true;
  }


  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {

    // processing lwbd
//    if (!processing && screenTimer > 2f) {
//      try {
//        setupLWBD();
//      } catch (IOException e) {
//        e.printStackTrace();
//      } catch (ClassNotFoundException e) {
//        e.printStackTrace();
//      }
//
////      try {
////        setupTarsos();
////      } catch (IOException e) {
////        e.printStackTrace();
////      } catch (UnsupportedAudioFileException e) {
////        e.printStackTrace();
////      }
//
//      screenTimer = 0f;
//    }

    // set up with json
    if (!processing && screenTimer > 2f) {
      try {
        setupLWBDWBD();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

//      try {
//        setupTarsos();
//      } catch (IOException e) {
//        e.printStackTrace();
//      } catch (UnsupportedAudioFileException e) {
//        e.printStackTrace();
//      }

      screenTimer = 0f;
    }

    screenTimer += delta;

    // when audio is processed start game
    if (processed && screenTimer > 2f) {
      if (level == 1) {
        game.setScreen(new PlayScreen((RhythmKnight) game));
        dispose();
      }
      else if (level == 2) {
        game.setScreen(new PlayScreen_2((RhythmKnight) game));
        dispose();
      } else if (level == 3) {
        game.setScreen(new PlayScreen_3((RhythmKnight) game));
        dispose();
      }
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
