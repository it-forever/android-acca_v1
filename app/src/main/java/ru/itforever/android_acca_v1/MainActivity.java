package ru.itforever.android_acca_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button stopPlaying, send, record, stop;
    private AudioRecord audiorecord;
    private static int SAMPLER = 44100; //Sample Audio Rate
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(SAMPLER, channelConfig, audioFormat);
    private boolean status = true;
    URI uri = URI.create("ws://192.168.50.15:3000");
    private String outputFile = null;
    private Thread senderThread = null;
    private WebSocketClient mWebSocket;
    private File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        file = new File(Environment.getExternalStorageDirectory(), "recording.wav");
        audiorecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLER, channelConfig, audioFormat, minBufSize);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);
        stop = (Button) findViewById(R.id.stop);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                AudioTask task = new AudioTask();
                task.execute();
                break;
            case R.id.stop:
                audiorecord.stop();
                mWebSocket.close();
                Log.d("Socket Closed", "");
            default:

                break;
        }
    }
}
public class AudioTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            WebSocketClient mWebSocket = new WebSocketClient(URI) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("Connected: ", mWebSocket.getConnection().toString());
                    mWebSocket.send("hello!");
                    byte[] buffer = new byte[minBufSize];
                    audiorecord.startRecording();
                    while (true) {
                        int number = audiorecord.read(buffer, 0, minBufSize);
                        for (int i = 0; i < number; i++) {
                            mWebSocket.send(buffer);
                        }
                    }
                }

                @Override
                public void onMessage(String message) {
                    Log.d("Received: ", message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("Closed", "Code = " + code + ", Reason: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.d("Error: ", ex.toString());
                }
            };
            mWebSocket.connect();
        } catch (Exception e) {
            Log.d("Exception: ", e.toString());
        }
        return null;
    }

    public void execute() {
    }
}