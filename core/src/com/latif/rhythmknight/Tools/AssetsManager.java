package com.latif.rhythmknight.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AssetsManager extends AssetManager {

  // Singleton
  private static AssetsManager instance;

  public static AssetsManager getInstance() {
    if (instance == null) {
      instance = new AssetsManager();
    }
    return instance;
  }

  public void init() {

    // create Asset Manager and load assets
    instance.load("audio/music/background.ogg", Music.class);
    instance.load("audio/music/background2.ogg", Music.class);
    instance.load("audio/song1.ogg", Music.class);
    instance.load("audio/song2.ogg", Music.class);
    instance.load("audio/song3.ogg", Music.class);
    instance.load("audio/song0.wav", Music.class);
    instance.load("audio/sounds/footsteps.wav", Sound.class);
    instance.load("audio/sounds/swordsound.wav", Music.class);
    instance.load("audio/sounds/meleesound.wav", Sound.class);
    instance.load("audio/sounds/breakblock.wav", Sound.class);
    instance.load("audio/sounds/goblingdie.wav", Sound.class);
    instance.load("audio/sounds/goblinghit.wav", Sound.class);
    instance.load("audio/sounds/rkdeath.wav", Sound.class);
    instance.load("audio/sounds/gamerestart.wav", Sound.class);
    instance.load("audio/music/gamewin.ogg", Music.class);
    instance.load("audio/music/gamewin2.wav", Music.class);
    instance.load("audio/music/gameover.ogg", Music.class);
    instance.load("audio/music/gameover2.wav", Music.class);
    instance.load("audio/music/titletheme.wav", Music.class);
    instance.load("audio/nightdistance.ogg", Music.class);
    instance.load("audio/basicdrum.ogg", Music.class);
    instance.load("audio/song1.mp3", Music.class);


    // finish loading assets
    instance.finishLoading();
  }

}
