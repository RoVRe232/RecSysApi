package com.upverasmusproject.ro.recsysrestapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CoreNLPStarter implements  Runnable{
    private static final Logger log = LoggerFactory.getLogger(CoreNLPStarter.class);

    @Override
    public void run() {

        try {
            if(Checker.portAvailable(9000)==true){
                Process proc = Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd "+GlobalVariables.CoreNLPPath +" && " +
                        "java -Xmx4g -cp \"*\" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -serverProperties StanfordCoreNLP-spanish.properties -port 9000 -timeout 15000\"");
            }
            else{
                log.warn("Did not start a new instance of CoreNLP, port "+9000+" is already in use, maybe by CoreNLP or other app," +
                        "see if there is an open instance of CoreNLP cmd, if not, change port number to an available port");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        PumpStreamHandler psh = new PumpStreamHandler(stdout);
        CommandLine cl = CommandLine.parse("cd /d d: && echo asdfg");
        DefaultExecutor exec = new DefaultExecutor();
        exec.setStreamHandler(psh);
        try {
            exec.execute(cl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(stdout.toString());
*/


/*
        ArrayList<String> commandsList = new ArrayList<String>();
        String stanfordServerPath = "D:\\MachineLearningSpania\\stanford-server";
        commandsList.add(new File(System.getProperty("java.home"), "bin/java").getAbsolutePath());
        //commandsList.add("-Djava.awt.headless=true");

        commandsList.add("-Xmx4g");
        commandsList.add("-cp");
        commandsList.add(new File(stanfordServerPath, "edu.stanford.nlp.pipeline.StanfordCoreNLPServer").getAbsolutePath());
        commandsList.add("-serverProperties StanfordCoreNLP-spanish.properties");
        //commandsList.add(new File(stanfordServerPath, "StanfordCoreNLP-spanish.properties").getAbsolutePath());
        commandsList.add("-port=9000");
        commandsList.add("-timeout=15000");

        ProcessBuilder pb = new ProcessBuilder(commandsList);
        try {
            //TODO use log4j2 instead of output files
            File outputLog = new File("D:\\MachineLearningSpania\\stanford-server\\CNLP-output.log");
            File errorLog = new File("D:\\MachineLearningSpania\\stanford-server\\CNLP-err.log");
            pb.redirectOutput(outputLog);
            pb.redirectError(errorLog);
            Process pr = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
