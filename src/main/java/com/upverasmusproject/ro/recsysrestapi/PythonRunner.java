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

    public Process runCommand(ArrayList<String> args, File outputFile){
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
}
