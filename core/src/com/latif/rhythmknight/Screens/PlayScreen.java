package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.latif.rhythmknight.Sprites.RKnight;
import com.latif.rhythmknight.Tools.B2WorldCreator;
import com.latif.rhythmknight.Tools.CutSceneController;

public class PlayScreen implements Screen {

  // reference to the game, used to set screens
  private RhythmKnight game;
  private TextureAtlas atlas;

  // reference to the Hud
  private Hud hud;

  // sprites
  private RKnight player;

  // Cutscene variables
  // boolean representing if cutscene has been executed
  private boolean cameraPositioned = false;
  private final float cameraStop = 5.0f;
  private final float cameraSpeed = 0.5f;

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
    // create TextureAtlas based on spritesheet
    atlas = new TextureAtlas("RKGraphics.pack");

    this.game = game;

    // create cam used to follow the game
    gameCam = new OrthographicCamera();

    // create FitViewport to maintain virtual aspect ratio despite screen size - scale to PPM
    gamePort = new FitViewport(RhythmKnight.V_WIDTH / RhythmKnight.PPM,
            RhythmKnight.V_HEIGHT / RhythmKnight.PPM, gameCam);

    // create game HUD for scores/hp/level
    hud = new Hud(game.batch);

    // create, load and render our game map
    mapLoader = new TmxMapLoader();
    map = mapLoader.load("level1.tmx");
    mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / RhythmKnight.PPM);

    // centre the gameCam to the correct aspect ratio at start of the game
    gameCam.position.set(gamePort.getWorldWidth() / 2f, gamePort.getWorldHeight() / 2f, 0);

    // set up Box2d World, with gravity
    world = new World(new Vector2(0, -10), true);
    b2dr = new Box2DDebugRenderer();

    // create B2WorldCreator
    new B2WorldCreator(this);

    // create RKnight in our game world for the active PlayScreen
    player = new RKnight(this);

  }

  public TextureAtlas getAtlas() {
    return atlas;
  }

  @Override
  public void show() {

  }

  public void animateCutsceneFrames(float deltaTime) {
    // Move screen to correct position at start of game
    // **Improve the implementation of this**
    if (gameCam.position.x < cameraStop) {
      gameCam.position.x += cameraSpeed * deltaTime;
    }
    // stop moving camera when in correct position
    if (gameCam.position.x > cameraStop) {
      cameraPositioned = true;
    }
    // move Player to correct position
    if (gameCam.position.x - 1.5f > player.b2body.getPosition().x && gameCam.position.x < cameraStop) {
      player.b2body.applyLinearImpulse(new Vector2(0.5f, 0f), player.b2body.getWorldCenter(), true);
    }
  }

  // Handle any key inputs or events
  public void handleInput(float deltaTime) {

    // play cutscene at beginning of game
    if (!cameraPositioned) {
      animateCutsceneFrames(deltaTime);
    } else if (cameraPositioned) {
      player.readyToBattle = true;
    }

    // Handling input for RK actions
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
      player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (player.b2body.getLinearVelocity().x <= 2)) {
      player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && (player.b2body.getLinearVelocity().x >= -2)) {
      player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.X)) {
      player.executeSlash();
    }

    // MOVE GOBLING LEFT

  }

  public void update(float deltaTime) {

    // handles any key inputs or events
    handleInput(deltaTime);

    //  Tells the physics engine that 1/60th of a second has passed every time you call it.
    //  If your game loop is being called more than 60 times a second it will go fast; less than 60
    //  times a second and it'll be slow.
    // The number of times it gets called per second will depend on the speed of the underlying
    // hardware, so this method will end up in different behavior on different devices.
    world.step(1 / 60f, 6, 2);

    //update for player sprite
    player.update(deltaTime);

    // always update the camera at every iteration of our render cycle
    gameCam.update();

    // this will only render what the gameCam can see
    mapRenderer.setView(gameCam);
  }

  @Override
  public void render(float deltaTime) {

    // At each render cycle we call update first - separates update logic from render
    update(deltaTime);

    // clear the screen at each render with black
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // renders the game map to the current screen
    mapRenderer.render();

    // render our Box2dDebugLines
    b2dr.render(world, gameCam.combined);

    // set only what the game can see
    game.batch.setProjectionMatrix(gameCam.combined);

    // set up the batch for drawing
    game.batch.begin();

    player.draw(game.batch);

    // end batch drawing
    game.batch.end();

    // (setProjectionMatrix sets the project matrix used by this batch)
    // (combined is the combined projection and 4x4 view matrix)
    // set our batch to now draw what the Hud camera sees
    game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    hud.stage.draw();


  }

  // Called when application is resized. This happens at any point during a non paused state but
  // not before a call to create()
  @Override
  public void resize(int width, int height) {
    // important that the viewport is adjusted so that it knows what the actual screen size is
    gamePort.update(width, height);
  }

  public TiledMap getMap() {
    return map;
  }

  public World getWorld() {
    return world;
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
    map.dispose();
    mapRenderer.dispose();
    world.dispose();
    b2dr.dispose();
    hud.dispose();
  }
}
