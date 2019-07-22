package com.latif.rhythmknight.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latif.rhythmknight.RhythmKnight;
import com.latif.rhythmknight.Tools.ParallaxBackground;

public class TitleScreen implements Screen {

  private Viewport gamePort;
  private Stage stage;

  private Game game;

  Table table;

  // loads the map into the game
  private TmxMapLoader mapLoader;
  // reference to the map itself
  private TiledMap map;
  // renders map to the screen
  private OrthogonalTiledMapRenderer mapRenderer;
  // a camera with orthographic projection
  private OrthographicCamera gameCam;

  public TitleScreen(Game game) {
    this.game = game;
    // create cam used to follow the game
    gameCam = new OrthographicCamera();
    gamePort = new FitViewport(RhythmKnight.V_WIDTH,
            RhythmKnight.V_HEIGHT, gameCam);
    stage = new Stage(new FitViewport(RhythmKnight.V_WIDTH, RhythmKnight.V_HEIGHT, new OrthographicCamera()), ((RhythmKnight) game).batch);

    // create, load and render our game map
//    mapLoader = new TmxMapLoader();
//    map = mapLoader.load("title.tmx");
//    mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / RhythmKnight.PPM);

    // create parallax scrolling background
    Array<Texture> textures = new Array<Texture>();
    for(int i = 1; i <=5;i++){
      textures.add(new Texture(Gdx.files.internal("parallax/img"+i+".png")));
      textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
    }

    ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
    parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    parallaxBackground.setSpeed(0.1f);
    stage.addActor(parallaxBackground);

    // centre the gameCam to the correct aspect ratio at start of the game
    gameCam.position.set(gamePort.getWorldWidth() / 2f, gamePort.getWorldHeight() / 2f, 0);

    // create labels and table for the stage
    Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    table = new Table();
    table.center();
    table.setFillParent(true);

    Label rhythmKnightLabel = new Label("RHYTHM KNIGHT", font);
    Label pressStart = new Label("Touch To Start", font);
    stage.addActor(table);

    table.add(rhythmKnightLabel).expandX();
    table.row();
    table.add(pressStart).expandX().padTop(10f);

    // play title theme
    RhythmKnight.manager.get("audio/music/titletheme.wav", Music.class).setLooping(true);
    RhythmKnight.manager.get("audio/music/titletheme.wav", Music.class).play();
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {

    // Start game when screen is touched
    if (Gdx.input.isTouched()) {
      game.setScreen(new PlayScreen((RhythmKnight) game));
      RhythmKnight.manager.get("audio/music/titletheme.wav", Music.class).stop();
      dispose();
    }

    // always update the camera at every iteration of our render cycle
    gameCam.update();
    // this will only render what the gameCam can see
//    mapRenderer.setView(gameCam);

    // clear screen at each render cycle
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // renders the game map to the current screen
//    mapRenderer.render();

    // Draw background texture

    // (setProjectionMatrix sets the project matrix used by this batch)
    // (combined is the combined projection and 4x4 view matrix)
    // set our batch to now draw what the Hud camera sees
    ((RhythmKnight) game).batch.setProjectionMatrix(stage.getCamera().combined);
    // draws title to the screen
    stage.draw();
  }

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
    stage.dispose();
  }
}
