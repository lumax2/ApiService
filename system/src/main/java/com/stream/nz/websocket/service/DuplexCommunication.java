package com.stream.nz.websocket.service;

import javax.websocket.Session;

public interface DuplexCommunication {

    void pushMessage(Session session, String unEncryptMsg) ;
}
