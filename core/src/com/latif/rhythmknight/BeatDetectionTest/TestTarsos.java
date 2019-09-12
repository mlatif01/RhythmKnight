package com.latif.rhythmknight.BeatDetectionTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class TestTarsos {

  public static ArrayList<Float> pitchList = new ArrayList<Float>();

  public static void main(String[] args) throws IOException, UnsupportedAudioFileException, NegativeArraySizeException, LineUnavailableException {
    AudioDispatcher dispatcher;
    String fileName = "audio/song0.wav";
    File audioFile = new File(fileName);
    AudioFormat format = null;

    PitchDetectionHandler handler = new PitchDetectionHandler() {
      @Override
      public void handlePitch(PitchDetectionResult pitchDetectionResult,
                              AudioEvent audioEvent) {
        if (pitchDetectionResult.getPitch() > 150) {
          pitchList.add((float) audioEvent.getTimeStamp());
        }
        System.out.println(audioEvent.getTimeStamp() + " " + pitchDetectionResult.getPitch());
      }
    };

    dispatcher = AudioDispatcherFactory.fromFile(audioFile, 2048, 512);
    dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, 44100, 1024, handler));
    dispatcher.run();

    System.out.println(pitchList.size());
  }
}
