package com.latif.rhythmknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.latif.rhythmknight.Screens.TitleScreen;
import com.latif.rhythmknight.Tools.AssetsManager;

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

  public static final String STAGE_ONE_MUSIC = "audio/song1.ogg";
  public static final String STAGE_ONE_ALTERNATE_MUSIC = "audio/song2.ogg";
  public static final String STAGE_ONE_MP3 = "audio/song1.mp3";
  public static final String STAGE_ONE_ALTERNATE_MP3 = "audio/song2.mp3";
  public static final String NIGHT_DISTANCE_MP3 = "audio/nightdistance.mp3";
  public static final String NIGHT_DISTANCE_MUSIC = "audio/nightdistance.ogg";
  public static final String BASIC_DRUM_MP3 = "audio/basicdrum.mp3";
  public static final String BASIC_DRUM = "audio/basicdrum.ogg";
  public static final String STAGE_THREE_MP3 = "audio/song3.mp3";
  public static final String STAGE_THREE_MUSIC = "audio/song3.ogg";

  // use to switch between songs
  public static boolean switcher = false;

  // AssetManager will be used as a singleton
  public static AssetsManager manager;

  //pixels per metre
  public static final float PPM = 100;

  //  The container which stores our images. Public so all screens can access
  public SpriteBatch batch;

  // Used to initialise game and load resources
  @Override
  public void create() {

    // initialise batch
    batch = new SpriteBatch();

    // initialise AssetsManager singleton
    manager = AssetsManager.getInstance();
    manager.init();

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
