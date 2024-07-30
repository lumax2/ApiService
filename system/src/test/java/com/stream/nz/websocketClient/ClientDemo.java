package com.stream.nz.websocketClient;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;

@Slf4j
public class ClientDemo extends MaxusJwtTest {

    @Test
    public void client() throws Exception {
        MyWebSocketClient client = new MyWebSocketClient("ws://localhost:9500/ws/" + jwt, jwt);
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
