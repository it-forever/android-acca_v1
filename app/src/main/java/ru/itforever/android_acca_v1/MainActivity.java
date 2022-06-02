package ru.itforever.android_acca_v1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import ru.itforever.android_acca_v1.libraryws.WsClient;


public class MainActivity extends AppCompatActivity {
    private static TextView t;
    private TcpClient tc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!WsClient.isConnected()) {
            WsClient.createWsClient();
        }

        checkPermissons();
        t = (TextView)findViewById(R.id.textView5);
        t.setMovementMethod(new ScrollingMovementMethod());
        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                tc = new TcpClient(((EditText)findViewById(R.id.address)).getText().toString(), Settings.PORT);
                tc.start();
                view.setEnabled(false);
                findViewById(R.id.echo_button).setEnabled(false);
            }
        });
        findViewById(R.id.echo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                tc = new TcpClient(((EditText)findViewById(R.id.address)).getText().toString(), Settings.PORT+1);
                tc.start();
                view.setEnabled(false);
                findViewById(R.id.start_button).setEnabled(false);
            }
        });
        findViewById(R.id.exit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                System.exit(0);
            }
        });
    }

    public static void addText(String s){
        t.append("\n"+s);
    }

    public void WsConnect(View view) {
        WsClient.createWsClient();

    }

    private int send = 0;
    public void WsSend(View view){
        WsClient.wsClient.send(String.valueOf(send));
        send++;
    }

    public void WsSendMessage(View view){
        WsClient.wsClient.send("");

    }

    public void WsClose(View view){
        WsClient.wsClient.close();

    }


    private void checkPermissons() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean perimissionFlas = false;
            for (String permissionStr : permissions) {
                int per = ContextCompat.checkSelfPermission(this, permissionStr);
                if (per != PackageManager.PERMISSION_GRANTED) {
                    perimissionFlas = true;
                }
            }
            if (perimissionFlas) {
                ActivityCompat.requestPermissions(this, permissions, 321);
            }
        }
    }

}