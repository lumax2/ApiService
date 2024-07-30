package com.stream.nz.websocketClient;


import com.stream.nz.util.RsaAesUtils;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;


@Slf4j
public class MyWebSocketClient extends WebSocketClient {

    private String maxusJwt = null;

    public MyWebSocketClient(String url, String maxusJwt) throws URISyntaxException {
        super(new URI(url),new Draft_6455());
        this.maxusJwt = maxusJwt;
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        log.info("handshaking");
        for(Iterator<String> it = shake.iterateHttpFields(); it.hasNext();) {
            String key = it.next();
            log.info(key+":"+shake.getFieldValue(key));
        }
        super.getConnection();
    }

    /**
     * s is JSONString and can be parsed into Object
     * @param s
     */
    @Override
    public void onMessage(String s) {
        String decrypted = RsaAesUtils.decryptByPublicAes(s,maxusJwt);
        log.info("receive message from server: " + decrypted);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("onClose{} {} {}",i,s,b);
    }

    @Override
    public void onError(Exception e) {
        log.info("onError",e);
    }


}
