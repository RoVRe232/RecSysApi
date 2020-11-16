package com.upverasmusproject.ro.recsysrestapi;

import java.io.*;

public class QueryLogger implements Runnable{
    //TODO transform logs in json or xml
    private static final File outputFile = new File("D:\\MachineLearningSpania\\SearchForAQuery-master-v2\\queriesLog.txt");
    private BufferedWriter outputFileWriter;
    private String query;
    private String queryResult;

    public QueryLogger(String query, String queryResult) {
        this.query = query;
        this.queryResult = queryResult;
    }

    public synchronized void  setFileWriter() throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile, true);
        this.outputFileWriter = new BufferedWriter(fileWriter);
    }

    public synchronized void writeQueryResult(String query, String queryResult) throws IOException {
        setFileWriter();
        outputFileWriter.write(query+",\n");
        outputFileWriter.write(queryResult+"\n\n");
        outputFileWriter.close();
    }

    @Override
    public void run() {
        try {
            writeQueryResult(query,queryResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
