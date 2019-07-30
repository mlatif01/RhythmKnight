package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.latif.rhythmknight.Sprites.Enemy;
import com.latif.rhythmknight.Sprites.Gobling;
import com.latif.rhythmknight.Sprites.RKnight;
import com.latif.rhythmknight.Tools.B2WorldCreator;
import com.latif.rhythmknight.Tools.CutSceneController;
import com.latif.rhythmknight.Tools.LWBDBeatDetector;
import com.latif.rhythmknight.Tools.TarsosPitchDetector;
import com.latif.rhythmknight.Tools.WorldContactListener;

import java.util.ArrayList;

//import com.latif.rhythmknight.Tools.BeatDetector;

public class PlayScreen implements Screen {

  // beat lists for BEAT DETECTION
  private ArrayList<Float> lwbdBeatList;
  private ArrayList<Float> tarsosPitchList;

  // boolean variables for choosing between stages
  private boolean isStage_1 = true;
  private boolean isStage_2 = false;

  // reference to the game, used to set screens
  private RhythmKnight game;

  // reference to the texture atlas
  private TextureAtlas atlas;
  private TextureAtlas atlas_2;
  private TextureAtlas atlas_3;

  // reference to beat detector
//  private BeatDetector beatDetector;

  // reference to the Hud
  private Hud hud;

  // sprites
  private RKnight player;

  // music
  private Music music;

  // number of enemies spawned in the game
  private int enemiesSpawned;
  private int enemiesKilled;
  private int enemiesToKill;

  // Box2d variables
  private World world;
  // provides a graphical representation of fixtures and bodies within box2d world
  private Box2DDebugRenderer b2dr;

  // creates the box2d world objects
  private B2WorldCreator creator;

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

  // TODO
  // state timer for play screen
  private int beatCount = 0;
  private float deltaSpawnToPlayer;
  private float deltaFromSpawnedToPlayer;
  private float gameEndTimer;
  private float timer;
  private float gameTime;

  // Cutscene controller which initiates cutscenes
  private CutSceneController cutSceneController;

  // Constructor for initialising the playscreen - as we need to send the game to the screen
  public PlayScreen(RhythmKnight game) {

    // setup debug logger
    Gdx.app.setLogLevel(Application.LOG_DEBUG);

    // create TextureAtlas based on sprite sheets
    atlas = new TextureAtlas("RKGraphics.pack");
    atlas_2 = new TextureAtlas("slime_graphics.pack");
    atlas_3 = new TextureAtlas("rkgraphics2.pack");

    this.game = game;

    // create cam used to follow the game
    gameCam = new OrthographicCamera();

    // create FitViewport to maintain virtual aspect ratio despite screen size - scale to PPM
    gamePort = new FitViewport(RhythmKnight.V_WIDTH / RhythmKnight.PPM,
            RhythmKnight.V_HEIGHT / RhythmKnight.PPM, gameCam);

    // create, load and render our game map
    mapLoader = new TmxMapLoader();
    // logic to choose between stages
    if (isStage_1) {
      map = mapLoader.load("level1.tmx");
    } else if (isStage_2) {
      map = mapLoader.load("level2.tmx");
    }
    mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / RhythmKnight.PPM);

    // centre the gameCam to the correct aspect ratio at start of the game
    gameCam.position.set(gamePort.getWorldWidth() / 2f, gamePort.getWorldHeight() / 2f, 0);

    // set up Box2d World, with gravity
    world = new World(new Vector2(0, -10), true);

    // creates B2d world objects using PlayScreen
    creator = new B2WorldCreator(this);

    // create debug renderer
    b2dr = new Box2DDebugRenderer();

    // create entity objects in our game world for the active PlayScreen
    player = new RKnight(this);

    // create game HUD for scores/hp/level
    // takes in player hp as argument for the hud display
    hud = new Hud(game.batch);

    // identifying collision objects
    world.setContactListener(new WorldContactListener());

    // instantiation of BeatDetector class
//    beatDetector = new BeatDetector();

    // call this timer when gameEnds
    gameEndTimer = 0f;

    // TODO: Confirm if this needs to be used
    // delta between spawned and player
    deltaFromSpawnedToPlayer = 0f;

    // TODO: Confirm if this needds to be used
    // we use this to create enemy before beat is detected to synchronise with slash
    // current distance in time between spawned and player
    deltaSpawnToPlayer = 2.1f;

    // enemy spawn, kill, toKill variables
    enemiesSpawned = 0;
    enemiesKilled = 0;
    enemiesToKill = 50;

    // controls cut scene events
    cutSceneController = new CutSceneController(this);

    // set up music for the current Screen - LWBD METHOD
    music = RhythmKnight.manager.get(RhythmKnight.STAGE_ONE_MUSIC, Music.class);
    music.setVolume(0.7f);
    music.play();

    // BEAT DETECTION LISTS INSTANTIATION
    lwbdBeatList = LWBDBeatDetector.getBeatListCopy();
//    tarsosPitchList = TarsosPitchDetector.getPitchListCopy();
  }


  public boolean gameOver() {
    // TODO: Check if static is needed here
    if ((player.currentState == RKnight.State.DEAD && player.getStateTimer() > 3)) {
      // stop music when player dies
//      beatDetector.player.pause();
      music.stop();
      return true;
    } else if (enemiesKilled >= enemiesToKill && enemiesSpawned >= enemiesKilled && player.getStateTimer() > 4 && gameEndTimer > 3.0f) {
//      beatDetector.player.pause();
      music.stop();
      return true;
    } else {
      return false;
    }
  }

  // getter for atlases
  public TextureAtlas getAtlas() {
    return atlas;
  }

  public TextureAtlas getAtlas_2() {
    return atlas_2;
  }

  public TextureAtlas getAtlas_3() {
    return atlas_3;
  }


  @Override
  public void show() {

  }

  // Handle any key inputs or events
  public void handleInput(float deltaTime) {

    // Handling input for RK actions
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
      player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (player.b2body.getLinearVelocity().x <= 2)
            && player.canMove) {
      player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && (player.b2body.getLinearVelocity().x >= -2)
            && player.canMove) {
      player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
    }
    if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
      player.handleSlash();
    }

  }

  public void update(float deltaTime) {

    // play cutscene at beginning of game
    if (!cutSceneController.isCameraPositioned() && isStage_1) {
      cutSceneController.animateStartCutscene(deltaTime);
    } else if (cutSceneController.isCameraPositioned()) {
      player.setReadyToBattle(true);
    }

    // focus camera on player
    // TODO: isStage_1, Improve logic
    if (cutSceneController.isCameraPositioned() && gameCam.zoom > 0.7 && isStage_1) {
      gameCam.zoom -= 0.003;
      gameCam.position.y -= 0.003;
      gameCam.position.x -= 0.005;
    }

    // focus camera away from player and perform end cutscene
    if (enemiesKilled == enemiesToKill && isStage_1) {
//      player.readyToBattle = false;
      gameEndTimer += deltaTime;
      if (gameEndTimer > 1f) {
        cutSceneController.animateEndCutscene();
      }

    }

    // for testing
//    System.out.println("ENEMIES KILLED: " + enemiesKilled);
//    System.out.println("ENEMIES SPAWNED: " + enemiesSpawned);
    gameTime += deltaTime;
//    System.out.println(gameTime);

    timer += deltaTime;

    // ADD BEAT DETECTION LOGIC HERE
//    minimBeatDetector();
    lwbdBeatDetector();
//    tarsosPitchDetector();

    // handles any key inputs or events
    handleInput(deltaTime);

    //  Tells the physics engine that 1/60th of a second has passed every time you call it.
    //  If your game loop is being called more than 60 times a second it will go fast; less than 60
    //  times a second and it'll be slow.
    // The number of times it gets called per second will depend on the speed of the underlying
    // hardware, so this method will end up in different behavior on different devices.
    world.step(1 / 60f, 6, 2);

    //update for sprites
    player.update(deltaTime);

    // update Hud
    hud.update(deltaTime);

    for (Enemy enemy : creator.getGoblings()) {
      enemy.update(deltaTime);
    }

    // always update the camera at every iteration of our render cycle
    gameCam.update();

    // this will only render what the gameCam can see
    mapRenderer.setView(gameCam);

  }

  public void minimBeatDetector() {
//    // beat detector logic
//    beatDetector.beat.detect(beatDetector.player.mix);
//
//    // beat detection test
////    if(beatDetector.beat.isHat()) {
////      System.out.println("HAT");
////    }
//
////    if(beatDetector.beat.isSnare()) {
////      System.out.println("SNARE");
////    }
//
////    if (beatDetector.beat.isKick()) {
////      System.out.println("KICK");
////    }
//
//    // MINIM BEAT DETECTION
////     spawn enemies if beat detected
//    if (beatDetector.beat.isSnare()) {
//      Gdx.app.log("BEAT DETECT", "BEAT: " + (++beatCount));
//      // TODO: Improve this
//      if (map.getLayers().get(6).getObjects().iterator().hasNext() && cutSceneController.isCameraPositioned()
//              && enemiesSpawned < enemiesToKill) {
////        Gdx.app.log("Gobling", "Spawned at time: " + gameTime);
////        Gdx.app.log("Gobling", "Should spawn at time: " + (gameTime - deltaSpawnToPlayer));
//        creator.spawnGobling();
//        enemiesSpawned += 1;
//      }
//    }
  }

  public void lwbdBeatDetector() {
    // LWBD BEAT DETECTION
    if (gameTime > lwbdBeatList.get(0) && !cutSceneController.isCameraPositioned()) {
      System.out.println("GAME TIME=" + gameTime + "\nBEAT TIME=" + lwbdBeatList.get(0));
      lwbdBeatList.remove(0);
    } else if ((gameTime) > lwbdBeatList.get(0) && cutSceneController.isCameraPositioned() && enemiesSpawned < enemiesToKill) {
      System.out.println("GAME TIME=" + gameTime + "\nBEAT TIME=" + lwbdBeatList.get(0));
      System.out.println("SPAWN ENEMY");
      creator.spawnGobling();
      enemiesSpawned += 1;
      lwbdBeatList.remove(0);
    }
  }

  public void tarsosPitchDetector() {
    // TarsosDSP pitch detection logic
    if (gameTime > tarsosPitchList.get(0) && !cutSceneController.isCameraPositioned()) {
      System.out.println("GAME TIME=" + gameTime + "\nBEAT TIME=" + tarsosPitchList.get(0));
      tarsosPitchList.remove(0);
    } else if ((gameTime) > tarsosPitchList.get(0) && cutSceneController.isCameraPositioned() && enemiesSpawned < enemiesToKill) {
      System.out.println("GAME TIME=" + gameTime + "\nBEAT TIME=" + tarsosPitchList.get(0));
      System.out.println("SPAWN ENEMY");
      creator.spawnGobling();
      enemiesSpawned += 1;
      tarsosPitchList.remove(0);
    }

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
//    b2dr.render(world, gameCam.combined);

    // set only what the game can see
    game.batch.setProjectionMatrix(gameCam.combined);

    // set up the batch for drawing
    game.batch.begin();
    player.draw(game.batch);

    for (Enemy enemy : creator.getGoblings()) {
      enemy.draw(game.batch);
    }

    // end batch drawing
    game.batch.end();

    // (setProjectionMatrix sets the project matrix used by this batch)
    // (combined is the combined projection and 4x4 view matrix)
    // set our batch to now draw what the Hud camera sees
    game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    hud.stage.draw();

    if (gameOver()) {
//      RhythmKnight.manager.clear();
      player.setReadyToBattle(false);
      Gobling.resetDeath();
      game.setScreen(new GameOver(game));
      dispose();
    }

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
    map.dispose();
    mapRenderer.dispose();
    world.dispose();
    b2dr.dispose();
    hud.dispose();
  }

  public B2WorldCreator getCreator() {
    return creator;
  }

  public TiledMap getMap() {
    return map;
  }

  public World getWorld() {
    return world;
  }

  public float getSpawnTimer() {
    return deltaFromSpawnedToPlayer;
  }

  public void resetSpawnTimer() {
    deltaFromSpawnedToPlayer = 0;
  }

  public void incrementEnemiesKilled() {
    enemiesKilled += 1;
  }

  public OrthographicCamera getGameCam() {
    return gameCam;
  }

  public RKnight getPlayer() {
    return player;
  }

}
