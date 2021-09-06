package com.upverasmusproject.ro.recsysrestapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {

    InputStream is;
    String type;

    /**
     *
     * @param is InputStream
     * @param type The type: "ERROR", etc.
     */
    public StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ((line = br.readLine()) != null) {
                //Do nothing.
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}