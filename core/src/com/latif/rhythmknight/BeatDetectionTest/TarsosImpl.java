//package com.latif.rhythmknight.BeatDetectionTest;
//
//import java.io.File;
//import java.io.IOException;
//
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.UnsupportedAudioFileException;
//
//import be.tarsos.dsp.AudioDispatcher;
//import be.tarsos.dsp.AudioEvent;
//import be.tarsos.dsp.AudioProcessor;
//import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
//
//public class TarsosImpl implements AudioProcessor {
//
//    AudioDispatcher dispatcher;
//
//    public TarsosImpl() throws IOException, UnsupportedAudioFileException {
//        String fileName = "audio/song1.mp3";
//        File audioFile = new File(fileName);
//        AudioFormat format = null;
//
//        try {
//            dispatcher = AudioDispatcherFactory.fromFile(audioFile, 4096*100, 0);
//            format = AudioSystem.getAudioFileFormat(audioFile).getFormat();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        new Thread(dispatcher,"Audio dispatching").start();
//
//    }
//
//    @Override
//    public boolean process(AudioEvent audioEvent) {
//        return false;
//    }
//
//    @Override
//    public void processingFinished() {
//
//    }
//}
