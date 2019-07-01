package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Scenes.Hud;
import com.latif.rhythmknight.Sprites.RKnight;

public class PlayScreen implements Screen {

  // Field variables
  private RhythmKnight game;
  private Hud hud;

  // create RKnight player sprite
  private RKnight player;

  // variable used to position the screen (**Improve this implementation**)
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
    // create FitViewport to maintain virtual aspect ratio despite screen size - scale to PPM
    gamePort = new FitViewport(RhythmKnight.V_WIDTH / RhythmKnight.PPM,
            RhythmKnight.V_HEIGHT / RhythmKnight.PPM, gameCam);
    // create game HUD for scores/hp/level
    hud = new Hud(game.batch);
    // create, load and render our game map
    mapLoader = new TmxMapLoader();
    map = mapLoader.load("level1.tmx");
    mapRenderer = new OrthogonalTiledMapRenderer(map, 1/RhythmKnight.PPM);
    // centre the gamecam to the correct aspect ratio at start of the game
    gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

    // set up Box2d World, with gravity
    world = new World(new Vector2(0, -10), true);
    b2dr = new Box2DDebugRenderer();
    player = new RKnight(world);

    // Adding bodies and fixtures to the game world (This will need to be put in separate classes)
    // A definition of what the body, fixtures consists of
    BodyDef bdef = new BodyDef();
    PolygonShape shape = new PolygonShape();
    FixtureDef fdef = new FixtureDef();
    Body body;

    // create ground bodies/fixtures - scale to PPM
    for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();

      bdef.type = BodyDef.BodyType.StaticBody;
      bdef.position.set((rect.getX() + rect.getWidth() / 2) / RhythmKnight.PPM, (rect.getY() + rect.getHeight() / 2) / RhythmKnight.PPM);
      body = world.createBody(bdef);

      shape.setAsBox((rect.getWidth() / 2) / RhythmKnight.PPM, (rect.getHeight() / 2) / RhythmKnight.PPM);
      fdef.shape = shape;
      body.createFixture(fdef);
    }


  }

  @Override
  public void show() {

  }

  public void update(float deltaTime) {
    // handles any key inputs or events
    handleInput(deltaTime);

    //  Tells the physics engine that 1/60th of a second has passed every time you call it.
    //  If your game loop is being called more than 60 times a second it will go fast; less than 60
    //  times a second and it'll be slow.
    // The number of times it gets called per second will depend on the speed of the underlying
    // hardware, so this method will end up in different behavior on different devices.
    world.step(1/60f, 6, 2);

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
    // **Improve the implementation of this**
    if (eCount < 120) {
      gameCam.position.x += 1 * deltaTime;
      eCount += 1;
    }

    // TEST
    // Handling input for RK movement
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
      player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (player.b2body.getLinearVelocity().x <= 2)) {
      player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && (player.b2body.getLinearVelocity().x >= -2)) {
      player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
    }

  }

  @Override
  public void render(float delta) {
    // At each render cycle we call update first - separates update logic from render
    update(delta);

    // clear the screen at each render with black
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // renders the game map to the current screen
    mapRenderer.render();

    // render our Box2dDebugLines
    b2dr.render(world, gameCam.combined);

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
