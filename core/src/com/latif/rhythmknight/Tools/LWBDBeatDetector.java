package com.latif.rhythmknight.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.latif.rhythmknight.RhythmKnight;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import featherdev.lwbd.Beat;
import featherdev.lwbd.BeatDetector;
import featherdev.lwbd.decoders.GdxMp3Decoder;
import featherdev.lwbd.decoders.GdxOggDecoder;
import featherdev.lwbd.decoders.JLayerMp3Decoder;
import featherdev.lwbd.decoders.LwbdDecoder;

public class LWBDBeatDetector {

  public static ArrayList<Float> beatList;

  public LWBDBeatDetector(String fileName) throws IOException, ClassNotFoundException {

    beatList = new ArrayList<Float>();

    // BEAT DETECTION LWBD PROCESSING
    // improve this impl. Create song manager
    FileHandle song = null;
    try {
      if (!RhythmKnight.switcher) {
        // android\assets\ (Add this when creating jar file)
        song = Gdx.files.internal("audio\\song1.mp3");
      } else {
        song = Gdx.files.internal("audio\\song2.mp3");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    LwbdDecoder decoder = null;
    try {
      decoder = new JLayerMp3Decoder(song.file());
    } catch (Exception e) {
      e.printStackTrace();
    }

    LinkedList<Beat> rawbeats = null;
    LinkedList<Beat> beatmap = null;

    System.out.println("processing...");
    rawbeats = featherdev.lwbd.BeatDetector.detectBeats(decoder, BeatDetector.SENSITIVITY_STANDARD);

    // filter beats
    beatmap = featherdev.lwbd.processing.BeatsProcessor.thinBeats(rawbeats, 250);
    beatmap = featherdev.lwbd.processing.BeatsProcessor.dropWeakBeats(beatmap, 0.3f);

    Iterator i$ = beatmap.iterator();

    while (i$.hasNext()) {
      Beat b = (Beat) i$.next();
      System.out.print("Time: " + (float) b.timeMs / 1000.0F + "s");
      System.out.print("\tEnergy: " + b.energy + "\n");
      beatList.add(b.timeMs / 1000.f);
    }
    System.out.println("SIZE OF LIST: " + beatList.size());

  }

  public static ArrayList<Float> getBeatListCopy() {
    return (ArrayList<Float>) beatList.clone();
  }

  public static void write() throws IOException {
//    FileOutputStream fos = new FileOutputStream("beatlist.txt");
//    ObjectOutputStream oos = new ObjectOutputStream(fos);
//    oos.writeObject(beatList);
//    oos.close();
    Json json = new Json();
    json.addClassTag("String", String.class); // This may not be needed. I don't know how json deals with String
    String beats = json.toJson(beatList);
    FileHandle file = Gdx.files.local("beats2.json");
    file.writeString(beats, false);
    System.out.println("WRITTEN TO TEXT FILE");
  }

  public static void readSong1() throws IOException, ClassNotFoundException {

    FileHandle file = Gdx.files.internal("beats.json");
    JsonValue json = new JsonReader().parse(file);
    JsonValue.JsonIterator it = json.iterator();
    beatList = new ArrayList<Float>();

    int count = 0;
    while (it.hasNext() && count < 100) {
      beatList.add(it.next().getFloat("value"));
      count++;
    }
  }

  public static void readSong2() throws IOException, ClassNotFoundException {

    FileHandle file = Gdx.files.internal("beats2.json");
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
