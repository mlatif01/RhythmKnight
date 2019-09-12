package com.latif.rhythmknight.BeatDetectionTest;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.latif.rhythmknight.Tools.B2WorldCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.Minim;
import ddf.minim.Recordable;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.javax.sound.sampled.AudioFormat;
import ddf.minim.spi.AudioOut;
import ddf.minim.spi.AudioRecording;
import ddf.minim.spi.AudioRecordingStream;
import ddf.minim.spi.AudioStream;
import ddf.minim.spi.MinimServiceProvider;
import ddf.minim.spi.SampleRecorder;


public class BeatDetector implements MinimServiceProvider {

  Minim minim;
  public AudioPlayer player;
  public AudioBuffer audioBuffer;
  public BeatDetect beat;

  public AudioPlayer getPlayer() {
    return player;
  }

  public BeatDetector() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG);
    setup();
  }

  /**
   * Override required method.
   */
  public String sketchPath(String fileName) {
    return (new File(fileName).getAbsolutePath());
  }

  /**
   * Override required method.
   */
  public InputStream createInput(String fileName)
          throws FileNotFoundException {
    return (new FileInputStream(new File(fileName)));
  }

  B2WorldCreator creator;
  int enemiesSpawned;

  private void setup() {

    minim = new Minim(this);
    player = minim.loadFile("audio/song1.mp3", 1024);
    player.play();
    beat = new BeatDetect(1024, 44100.0f);
  }

  public void update(float deltaTime) {

    if (player.mix.level() > 0.1) {
      Gdx.app.log("BEAT", "");
      creator.spawnGobling();
      enemiesSpawned += 1;
    }

  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  @Override
  public void debugOn() {

  }

  @Override
  public void debugOff() {

  }

  @Override
  public AudioRecording getAudioRecording(String s) {
    return null;
  }

  @Override
  public AudioRecordingStream getAudioRecordingStream(String s, int i, boolean b) {
    return null;
  }

  @Override
  public AudioStream getAudioInput(int i, int i1, float v, int i2) {
    return null;
  }

  @Override
  public AudioOut getAudioOutput(int i, int i1, float v, int i2) {
    return null;
  }

  @Override
  public AudioSample getAudioSample(String s, int i) {
    return null;
  }

  @Override
  public AudioSample getAudioSample(float[] floats, AudioFormat audioFormat, int i) {
    return null;
  }

  @Override
  public AudioSample getAudioSample(float[] floats, float[] floats1, AudioFormat audioFormat, int i) {
    return null;
  }

  @Override
  public SampleRecorder getSampleRecorder(Recordable recordable, String s, boolean b) {
    return null;
  }
}
