package com.upverasmusproject.ro.recsysrestapi.services.interfaces;

import java.io.IOException;

public interface ICommunicationService {
    void sendDataToProcess(String data) throws IOException;
    String awaitResponseFromProcess();
}
