package com.upverasmusproject.ro.recsysrestapi.services;

import com.upverasmusproject.ro.recsysrestapi.RecsysRestapiApplication;
import com.upverasmusproject.ro.recsysrestapi.services.interfaces.ICommunicationService;
import com.upverasmusproject.ro.recsysrestapi.utils.StreamGobbler;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

public class CommunicationService implements ICommunicationService {
    private String _communicationToken;

    private OutputStreamWriter _queryProcessorWriter;
    private BufferedReader _queryProcessorReader;
    private StreamGobbler _queryProcessorErrGobbler;

    public CommunicationService(Process queryProcessor){
        _queryProcessorWriter = new OutputStreamWriter(queryProcessor.getOutputStream());
        _queryProcessorReader = new BufferedReader(new InputStreamReader(queryProcessor.getInputStream()));
        _queryProcessorErrGobbler = new StreamGobbler(queryProcessor.getErrorStream(), "ERROR");

        _communicationToken = generateCommunicationToken();
    }

    private String generateCommunicationToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void sendDataToProcess(String data) throws IOException {
//        _queryProcessorWriter.write(data+' '+_communicationToken+'\n');
//        _queryProcessorWriter.flush();

        URL url = new URL("http://0.0.0.0:5005/videos");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        byte[] out = ("{\"keywords\":\""+data+"\"}").getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));

        String responseLine;
        do {
            responseLine = reader.readLine();
            System.out.println(responseLine);
        }while(responseLine!=null);
    }

    @Override
    public String awaitResponseFromProcess() {
        StringBuilder queryResult = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        Thread errThread = new Thread(_queryProcessorErrGobbler);
        errThread.start();

        try{
            //TODO add support for spanish characters
            String line;
            while((line=_queryProcessorReader.readLine())!= null){
                System.out.println("Appended line: "+line);
                sb.append(line);
                if(line.startsWith(_communicationToken + "("))
                    queryResult.append(line+'\n');
                sb.append(System.getProperty("line.separator"));
                if(line.equals("eoq "+_communicationToken))
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            errThread.interrupt();
        }

        return queryResult.toString();
    }

}
