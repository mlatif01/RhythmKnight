package com.latif.rhythmknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.latif.rhythmknight.Screens.TitleScreen;

public class RhythmKnight extends Game {

  // Virtual width and height for the game
  public static final int V_WIDTH = 400;
  public static final int V_HEIGHT = 208;

  // category bit filters
  public static final short GROUND_BIT = 1;
  public static final short RKNIGHT_BIT = 2;
  public static final short STONE_BIT = 4;
  public static final short DESTROYED_BIT = 8;
  public static final short GOBLING_BIT = 16;
  public static final short OBJECT_BIT = 32;
  public static final short GOBLING_HEAD_BIT = 64;
  public static final short SWORD_BIT = 128;

  /* AssetManager will be used in a static context to save time */
  public static AssetManager manager;

  //pixels per metre
  public static final float PPM = 100;

  //  The container which stores our images. Public so all screens can access
  public SpriteBatch batch;

  // Used to initialise game and load resources
  @Override
  public void create() {

    // initialise batch
    batch = new SpriteBatch();

    // create Asset Manager and load assets
    manager = new AssetManager();
    manager.load("audio/music/background.ogg", Music.class);
    manager.load("audio/music/background2.ogg", Music.class);
    manager.load("audio/sounds/footsteps.wav", Sound.class);
    manager.load("audio/sounds/swordsound.wav", Music.class);
    manager.load("audio/sounds/meleesound.wav", Sound.class);
    manager.load("audio/sounds/breakblock.wav", Sound.class);
    manager.load("audio/sounds/goblingdie.wav", Sound.class);
    manager.load("audio/sounds/goblinghit.wav", Sound.class);
    manager.load("audio/sounds/rkdeath.wav", Sound.class);
    manager.load("audio/sounds/gamerestart.wav", Sound.class);
    manager.load("audio/music/gamewin.ogg", Music.class);
    manager.load("audio/music/gameover.ogg", Music.class);

    // finish loading assets
    manager.finishLoading();

    // pass in this so that the PlayScreen can set screens
    setScreen(new TitleScreen(this));
  }

  // used to update and render the game elements called 60 times per second
  @Override
  public void render() {
    // delegates the render method to the active screen
    super.render();
  }

  // used to free up resources used in the game
  @Override
  public void dispose() {
    // release all resources of the object
    batch.dispose();
  }
}
