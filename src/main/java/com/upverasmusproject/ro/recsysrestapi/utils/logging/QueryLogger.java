package com.upverasmusproject.ro.recsysrestapi.utils.logging;

import com.upverasmusproject.ro.recsysrestapi.utils.GlobalVariables;
import com.upverasmusproject.ro.recsysrestapi.entities.User;

import java.io.*;

public class QueryLogger implements Runnable{
    private static final File outputFile = new File(GlobalVariables.LogFile);
    private BufferedWriter outputFileWriter;
    private String query;
    private String queryResult;
    private User user;
    private String oldQueryResult;

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
            outputFileWriter.write("{\"user\":"+user.toString()+",\n");
        else
            outputFileWriter.write("{\"user\":"+"0"+",\n");

        outputFileWriter.write("\"query\":\""+query+"\",\n");
        outputFileWriter.write("\"oldqueryresult\":"+oldQueryResult+",\n");
        outputFileWriter.write("\"queryresult\":"+queryResult.replace("\"\"","\"")+"},\n");
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
