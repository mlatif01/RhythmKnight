package com.latif.rhythmknight.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import featherdev.lwbd.Beat;
import featherdev.lwbd.BeatDetector;
import featherdev.lwbd.decoders.JLayerMp3Decoder;
import featherdev.lwbd.decoders.LwbdDecoder;

public class LWBDBeatDetector {

  public static ArrayList<Float> beatList;

  public LWBDBeatDetector(String fileName, int level) throws IOException, ClassNotFoundException {

    beatList = new ArrayList<Float>();
    FileHandle song = null;
    LinkedList<Beat> rawbeats = null;
    LinkedList<Beat> beatmap = null;
    LwbdDecoder decoder = null;

    try {
      if (level == 1) {
        // android\assets\ (Add this when creating jar file)
        song = Gdx.files.internal("audio\\song1.mp3");
      } else if (level == 2) {
        song = Gdx.files.internal("audio\\song2.mp3");
      } else if (level == 3) {
        song = Gdx.files.internal("audio\\song3.mp3");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      decoder = new JLayerMp3Decoder(song.file());
    } catch (Exception e) {
      e.printStackTrace();
    }

    // process raw beats
    System.out.println("processing...");
    rawbeats = featherdev.lwbd.BeatDetector.detectBeats(decoder, BeatDetector.SENSITIVITY_STANDARD);

    // filter beats
    beatmap = featherdev.lwbd.processing.BeatsProcessor.thinBeats(rawbeats, 250);
    beatmap = featherdev.lwbd.processing.BeatsProcessor.dropWeakBeats(beatmap, 0.3f);

    Iterator iterator = beatmap.iterator();

    while (iterator.hasNext()) {
      Beat b = (Beat) iterator.next();
      // print time of beat
      System.out.print("Time: " + (float) b.timeMs / 1000.0F + "s");
      // print energy of the beat
      System.out.print("\tEnergy: " + b.energy + "\n");
      // time in seconds
      beatList.add(b.timeMs / 1000.f);
    }

    // method to write beat list values to json - use when adding new stages
//    write();

    System.out.println("SIZE OF LIST: " + beatList.size());

  }

  public static ArrayList<Float> getBeatListCopy() {
    return (ArrayList<Float>) beatList.clone();
  }

  public static void write() throws IOException {
    Json json = new Json();
    json.addClassTag("String", String.class);
    String beats = json.toJson(beatList);
    FileHandle file = Gdx.files.local("beats3.json");
    file.writeString(beats, false);
  }

  public static void readSong(int level) throws IOException, ClassNotFoundException {
    FileHandle file = Gdx.files.internal("beats" + level + ".json");
    JsonValue json = new JsonReader().parse(file);
    JsonValue.JsonIterator it = json.iterator();
    beatList = new ArrayList<Float>();
    int count = 0;
    while (it.hasNext() && count < 100) {
      beatList.add(it.next().getFloat("value"));
      count++;
    }
  }


}
