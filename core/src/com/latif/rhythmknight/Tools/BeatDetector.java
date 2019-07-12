package com.latif.rhythmknight.Tools;

import com.badlogic.gdx.Game;
import com.latif.rhythmknight.Screens.PlayScreen;
import com.latif.rhythmknight.Sprites.RKnight;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ddf.minim.*;
import ddf.minim.analysis.BeatDetect;

import be.tarsos.dsp.*;

public class BeatDetector {

  Minim minim;
  public AudioPlayer player;
  public AudioBuffer audioBuffer;
  public BeatDetect beat;

  float eRadius;

  public BeatDetector(){
    setup();
  }

  /**
   * Override required method.
   */
  public String sketchPath(String fileName)
  {
    return( new File(fileName).getAbsolutePath() );
  }

  /**
   * Override required method.
   */
  public InputStream createInput(String fileName)
          throws FileNotFoundException
  {
    return( new FileInputStream(new File(fileName)) );
  }

  private void setup() {

    minim = new Minim(this);
    player = minim.loadFile("audio/song1.mp3", 1024);
    player.play();
    beat = new BeatDetect(1024, 44100.0f);
    beat.setSensitivity(300);

    eRadius = 20;
  }

  public void update(float deltaTime) {

    if (player.mix.level() > 0.1) {
      System.out.println("BEAT");
    }

  }

}
