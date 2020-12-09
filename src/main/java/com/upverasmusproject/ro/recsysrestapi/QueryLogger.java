package com.upverasmusproject.ro.recsysrestapi;

import java.io.*;

public class QueryLogger implements Runnable{
    private static final File outputFile = new File("D:\\MachineLearningSpania\\SearchForAQuery-master-v2\\queriesLog.txt");
    private BufferedWriter outputFileWriter;
    private String query;
    private String queryResult;
    private User user;
    private String oldQueryResult;

    public QueryLogger(String query, String queryResult) {
        this.query = query;
        this.queryResult = queryResult;
        this.user = null;
    }

    public QueryLogger(String query, String queryResult, User user) {
        this.query = query;
        this.queryResult = queryResult;
        this.user = user;
    }

    public QueryLogger(String query, String queryResult, User user, String oldQueryResult) {
        this.query = query;
        this.queryResult = queryResult;
        this.user = user;
        this.oldQueryResult = oldQueryResult;
    }

    public synchronized void  setFileWriter() throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile, true);
        this.outputFileWriter = new BufferedWriter(fileWriter);
    }

    public synchronized void writeQueryResult(String query, String queryResult, User user) throws IOException {
        setFileWriter();
        if(user!=null)
            outputFileWriter.write("[\"user\":"+user.toString()+",\n");
        outputFileWriter.write("\"query\":\""+query+"\",\n");
        outputFileWriter.write("\"oldqueryresult\":"+oldQueryResult+",\n");
        outputFileWriter.write("\"queryresult:"+queryResult+"]]\n");
        outputFileWriter.close();
    }

    @Override
    public void run() {
        try {
            writeQueryResult(query,queryResult, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
