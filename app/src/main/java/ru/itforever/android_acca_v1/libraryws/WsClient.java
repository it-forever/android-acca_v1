package ru.itforever.android_acca_v1.libraryws;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class WsClient extends WebSocketClient {
    public static WsClient wsClient;
    public static boolean isConnected = false;

    /**
     * Initialize all the variables
     *
     * @param uri URI of the WebSocket server
     */
    public WsClient(URI uri) {
        super(uri);
    }

    public static void createWsClient() {
            URI uri;
            try {
                uri = new URI("ws://192.168.50.15:8080/websocket");
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }

            if (isConnected()) {
                return;
            }

            wsClient = new WsClient(uri) {
                @Override
                public void onOpen() {
                    System.out.println("onOpen");
                    wsClient.send("I'am. Я смог подключится к вашему websocket. Прям смог. Уххх.");
                }

                @Override
                public void onTextReceived(String message) {
                    System.out.println("onTextReceived");
                }

                @Override
                public void onBinaryReceived(byte[] data) {
                    System.out.println("onBinaryReceived");
                }

                @Override
                public void onPingReceived(byte[] data) {
                    System.out.println("onPingReceived");
                }

                @Override
                public void onPongReceived(byte[] data) {
                    System.out.println("onPongReceived");
                }

                @Override
                public void onException(Exception e) {
                    System.out.println(e.getMessage());
                }

                @Override
                public void onCloseReceived() {
                    System.out.println("onCloseReceived");
                }
            };

            wsClient.setConnectTimeout(10000);
            wsClient.setReadTimeout(60000);
            wsClient.addHeader("Origin", "http://developer.it-forever.ru");
            wsClient.enableAutomaticReconnection(5000);
            wsClient.connect();
        }



    public static boolean isConnected() {
        return isConnected;
    }

    @Override
    public void onOpen() {
        isConnected = true;

    }

    @Override
    public void onTextReceived(String message) {
        Log.i("onTextReceived", message);

    }

    @Override
    public void onBinaryReceived(byte[] data) {

    }

    @Override
    public void onPingReceived(byte[] data) {

    }

    @Override
    public void onPongReceived(byte[] data) {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onCloseReceived() {
        isConnected = false;

    }
}
