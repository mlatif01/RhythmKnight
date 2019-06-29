package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latif.rhythmknight.RhythmKnight;

public class PlayScreen implements Screen {

  // Field variables
  private RhythmKnight game;
  Texture texture;

  // a camera with orthographic projection
  private OrthographicCamera gameCam;

  // Manages a Camera and determines how world coordinates are mapped to and from the screen
  private Viewport gamePort;

  // Constructor for initialising the playscreen
  public PlayScreen(RhythmKnight game) {
    this.game = game;
    texture = new Texture("badlogic.jpg");
    gameCam = new OrthographicCamera();

    // FitViewport also supports a virtual screen size to maintain aspect ratio
    // It keeps the aspect ratio by scaling the world up to fit the screen, adding black bars for
    // remaining space
    gamePort = new FitViewport(800, 480, gameCam);
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // tell the game batch to recognise where the camera is in the game world
    // (setProjectionMatrix sets the project matrix used by this batch)
    // (combined is the combined projection and 4x4 view matrix)
    game.batch.setProjectionMatrix(gameCam.combined);

    // set up the batch for drawing
    game.batch.begin();

    // draws texture to screen
    game.batch.draw(texture, 0, 0);

    // end
    game.batch.end();
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
