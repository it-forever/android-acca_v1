package ru.itforever.android_acca_v1;


import android.media.AudioFormat;
import android.media.AudioRecord;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TcpClient extends Thread {
    private String TAG = "TcpClient";
    private String serverIP;
    private int serverPort;
    private PrintWriter pw;
    private InputStream is;
    private DataInputStream dis;
    private boolean isRun;
    private Socket socket = null;
    byte buff[];//  = new byte[4096];
    private String rcvMsg;
    private int rcvLen;

    public TcpClient(String ip , int port){
        this.serverIP = ip;
        this.serverPort = port;
        this.isRun = false;
        buff = new byte[Settings.getBuffSize()];
        MainActivity.addText("инициализация....");
    }

    public void close(){
        try {
            pw.close();
            is.close();
            dis.close();
            socket.close();
            isRun = false;
            AudioPlayer.getInstance().stop();
            MainActivity.addText("Отключено, перезапустите приложение");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg){
        pw.println(msg);
        pw.flush();
    }

    public void send(byte[] b){
        try {
            socket.getOutputStream().write(b);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
            isRun = false;
        }
    }

    @Override
    public void run() {
        try {
            MainActivity.addText("таймаут подключения....");
            socket = new Socket(serverIP,serverPort);
            socket.setSoTimeout(Settings.TCP_TIMEOUT);
            pw = new PrintWriter(socket.getOutputStream(),true);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            isRun = true;
        } catch(ConnectException e){
            MainActivity.addText("Время соединения истекло！");
            return;
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }
        MainActivity.addText("Соединение успешно！");
        AudioRecorder ar = new AudioRecorder(this);
        ar.start();
        AudioPlayer.getInstance().play();
        while (isRun){
            try {
                rcvLen = dis.read(buff);
                AudioPlayer.getInstance().playData(buff);
            } catch (SocketTimeoutException e){
            } catch (IOException e) {
                e.printStackTrace();
                MainActivity.addText("линия отключена");
                break;
            }
        }
        ar.close();
        this.close();
    }

    public boolean isRun() {
        return isRun;
    }

}
