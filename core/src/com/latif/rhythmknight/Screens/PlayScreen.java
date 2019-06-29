package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Scenes.Hud;

public class PlayScreen implements Screen {

  // Field variables
  private RhythmKnight game;
  private Hud hud;

  // a camera with orthographic projection
  private OrthographicCamera gameCam;

  // Manages a Camera and determines how world coordinates are mapped to and from the screen
  private Viewport gamePort;

  // Constructor for initialising the playscreen - as we need to send the game to the screen
  public PlayScreen(RhythmKnight game) {
    this.game = game;
    // create cam used to follow the game
    gameCam = new OrthographicCamera();
    // create FitViewport to maintain virtual aspect ratio despite screen size
    gamePort = new FitViewport(RhythmKnight.V_WIDTH, RhythmKnight.V_HEIGHT, gameCam);
    // create game HUD for scores/hp/level
    hud = new Hud(game.batch);

  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    // clear the screen at each render
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // tell the game batch to recognise where the camera is in the game world
    // (setProjectionMatrix sets the project matrix used by this batch)
    // (combined is the combined projection and 4x4 view matrix)
    game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    hud.stage.draw();
//
//    // set up the batch for drawing
//    game.batch.begin();
//
//    // end
//    game.batch.end();
  }

  // Called when application is resized. This happens at any point during a non paused state but
  // not before a call to create()
  @Override
  public void resize(int width, int height) {
    // important that the viewport is adjusted so that it knows what the actual screen size is
    gamePort.update(width, height);
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

  }
}
