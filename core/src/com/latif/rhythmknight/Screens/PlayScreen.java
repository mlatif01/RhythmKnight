package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Scenes.Hud;

public class PlayScreen implements Screen {

  // Field variables
  private RhythmKnight game;
  private Hud hud;

  // variable used to position the screen (Improve this implementation)
  private int eCount = 0;

  // Box2d variables
  private World world;
  // provides a graphical representation of fixtures and bodies within box2d world
  private Box2DDebugRenderer b2dr;

  // loads the map into the game
  private TmxMapLoader mapLoader;
  // reference to the map itself
  private TiledMap map;
  // renders map to the screen
  private OrthogonalTiledMapRenderer mapRenderer;

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
    // create, load and render our game map
    mapLoader = new TmxMapLoader();
    map = mapLoader.load("level1.tmx");
    mapRenderer = new OrthogonalTiledMapRenderer(map);
    // centre the gamecam to the correct aspect ratio at start of the game
    gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);


  }

  @Override
  public void show() {

  }

  public void update(float deltaTime) {
    // handles any key inputs or events
    handleInput(deltaTime);
    // always update the camera at every iteration of our render cycle
    gameCam.update();
    // this will only render what the gameCam can see
    mapRenderer.setView(gameCam);
  }

  // Handle any key inputs or events
  public void handleInput(float deltaTime) {
//    if (Gdx.input.isTouched()) {
//      gameCam.position.x += 100 * deltaTime;
//    }

    // Move screen to correct position at start of game
    // Improve the implementation of this
    if (eCount < 250) {
      gameCam.position.x += 50 * deltaTime;
      eCount += 1;
    }

  }

  @Override
  public void render(float delta) {
    // At each render cycle we call update first
    update(delta);

    // clear the screen at each render
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // renders the  map to the current screen
    mapRenderer.render();

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
