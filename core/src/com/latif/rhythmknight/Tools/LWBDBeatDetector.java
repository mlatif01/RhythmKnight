package com.latif.rhythmknight.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import featherdev.lwbd.Beat;
import featherdev.lwbd.BeatDetector;
import featherdev.lwbd.decoders.JLayerMp3Decoder;
import featherdev.lwbd.decoders.LwbdDecoder;

public class LWBDBeatDetector {

  public static ArrayList<Float> beatList = new ArrayList<Float>();

  public LWBDBeatDetector(String fileName) {
    // BEAT DETECTION LWBD PROCESSING
    File myAudioFile = new File(fileName);
    LwbdDecoder decoder = null;
    try {
      decoder = new JLayerMp3Decoder(myAudioFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    LinkedList<Beat> rawbeats = null;
    LinkedList<Beat> beatmap  = null;

    System.out.println("processing...");
    rawbeats = featherdev.lwbd.BeatDetector.detectBeats(decoder, BeatDetector.SENSITIVITY_STANDARD);

    // filter beats
    beatmap = featherdev.lwbd.processing.BeatsProcessor.thinBeats(rawbeats, 250);
    beatmap = featherdev.lwbd.processing.BeatsProcessor.dropWeakBeats(beatmap, 0.3f);

    Iterator i$ = beatmap.iterator();

    while(i$.hasNext()) {
      Beat b = (Beat)i$.next();
      System.out.print("Time: " + (float)b.timeMs / 1000.0F + "s");
      System.out.print("\tEnergy: " + b.energy + "\n");
      beatList.add(b.timeMs / 1000.f);
    }
  }

  public ArrayList getBeatList() {
    return beatList;
  }

}
