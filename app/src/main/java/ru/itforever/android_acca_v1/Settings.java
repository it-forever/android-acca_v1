package ru.itforever.android_acca_v1;


import android.media.AudioFormat;
import android.media.AudioRecord;

public class Settings {
    public static final String IP_ADDRESS = "192.168.50.15";
    public static final int PORT = 8080;
    //public static final int WEB_URI = "/websocket";
    public static final int SAMPLE_HZ = 48000;
    public static final int TCP_TIMEOUT = 5000;
    public static int getBuffSize(){
        return AudioRecord.getMinBufferSize(SAMPLE_HZ, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
    }

}
