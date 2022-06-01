package ru.itforever.android_acca_v1;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayer {

    private AudioTrack track;
    private static AudioPlayer ap = null;

    public static AudioPlayer getInstance(){
        if (ap == null){
            ap = new AudioPlayer();
        }
        return ap;
    }

    public AudioPlayer(){
        track = new AudioTrack(AudioManager.STREAM_MUSIC, Settings.SAMPLE_HZ, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, Settings.getBuffSize(), AudioTrack.MODE_STREAM);
    }

    public void play(){
        track.play();
    }

    public void stop(){
        track.flush();
    }

    public void playData(byte[] bytes){
        track.write(bytes, 0, Settings.getBuffSize());
    }
}
