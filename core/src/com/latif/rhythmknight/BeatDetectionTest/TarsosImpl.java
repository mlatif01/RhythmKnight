package com.latif.rhythmknight.BeatDetectionTest;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class TarsosImpl implements AudioProcessor {

    AudioDispatcher dispatcher;

    public TarsosImpl() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        String fileName = "audio/song1.mp3";
        File audioFile = new File(fileName);
        AudioFormat format = null;

        PitchDetectionHandler handler = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                    AudioEvent audioEvent) {
                System.out.println(audioEvent.getTimeStamp() + " " + pitchDetectionResult.getPitch());
            }
        };
        AudioDispatcher adp = AudioDispatcherFactory.fromFile(audioFile,2048, 0);
        adp.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, 44100, 2048, handler));
        adp.run();

    }

    @Override
    public boolean process(AudioEvent audioEvent) {
        return false;
    }

    @Override
    public void processingFinished() {

    }
}
