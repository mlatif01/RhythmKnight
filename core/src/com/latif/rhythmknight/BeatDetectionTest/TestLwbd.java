package com.latif.rhythmknight.BeatDetectionTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;

import featherdev.lwbd.Beat;
import featherdev.lwbd.BeatDetector;
import featherdev.lwbd.decoders.JLayerMp3Decoder;
import featherdev.lwbd.decoders.LwbdDecoder;

public class TestLwbd {

  public static void main(String[] args) throws FileNotFoundException {
    File myAudioFile = new File("audio/song1.mp3");
    LwbdDecoder decoder = new JLayerMp3Decoder(myAudioFile);
    System.out.println("processing...");
    LinkedList<Beat> beats = BeatDetector.detectBeats(decoder, BeatDetector.SENSITIVITY_AGGRESSIVE);
    Iterator i$ = beats.iterator();

    while (i$.hasNext()) {
      Beat b = (Beat) i$.next();
      System.out.print("Time: " + (float) b.timeMs / 1000.0F + "s");
      System.out.print("\tEnergy: " + b.energy + "\n");
    }

  }
}
