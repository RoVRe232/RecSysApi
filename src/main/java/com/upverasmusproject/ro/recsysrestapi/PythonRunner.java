package com.upverasmusproject.ro.recsysrestapi;


import java.io.*;
import java.util.ArrayList;

public class PythonRunner {

    private static PythonRunner pythonRunner = new PythonRunner();

    public static PythonRunner getInstance(){
        return PythonRunner.pythonRunner;
    }

    public ArrayList<String> buildSimplePythonRunnerCommand(String path){
        ArrayList<String> result = new ArrayList<>();
        result.add("python");
        result.add(path);
        return result;
    }


    /**
     *  Uses processBuilder to start a process using args
     * @param args
     * @return Process
     */
    public Process runCommand(ArrayList<String> args){
        Runtime runtime = Runtime.getRuntime();
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try {
            Process proc = processBuilder.start();
            return proc;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String readProcessOutput(InputStream is) throws IOException {
        String buffer = "";
        InputStreamReader isr = new InputStreamReader(is);
        try {
            int c;
            while ((c = isr.read()) >= 0) {
                buffer = buffer + (char) c;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static String invokeOldQuery(String searchedKeysBulk, int nrOfResults) throws IOException {
        ArrayList<String> command = new ArrayList<>();
        command.add("python");
        command.add(GlobalVariables.normalQueryPath);
        command.add(searchedKeysBulk);
        command.add("10");
        Process process = PythonRunner.getInstance().runCommand(command);
        String output = PythonRunner.getInstance().readProcessOutput(process.getInputStream());
        return output;
    }
}
