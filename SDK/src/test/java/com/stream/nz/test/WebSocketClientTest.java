package com.stream.nz.test;

import com.stream.nz.websocketClient.MyWebSocketClient;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;

public class WebSocketClientTest extends JwtTest {


    @Test
    public void client() throws Exception {
        MyWebSocketClient client = new MyWebSocketClient("wss://oversea.saicmaxus.com/australia/test/ws/" + jwt, jwt);
        client.connect();
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            System.out.println("wait to connect");
            Thread.sleep(1000);
        }
        System.out.println("webSocket established");
        while (client.isOpen()){
            Thread.sleep(5000);
        }
        client.close();
    }

}
