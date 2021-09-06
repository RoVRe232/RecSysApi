package com.upverasmusproject.ro.recsysrestapi.services;


import com.upverasmusproject.ro.recsysrestapi.utils.GlobalVariables;

import java.io.*;
import java.util.ArrayList;

public class PythonService {

    private static PythonService pythonService = new PythonService();
    public static PythonService getInstance(){
        return PythonService.pythonService;
    }

    /**
     *  Uses processBuilder to start a process using args
     * @param args
     * @return Process
     */
    public Process runCommand(ArrayList<String> args){
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try {
            return processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Process runSearchForQuery()  {
        try {
            return Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"conda activate recenv && cd "+//Add conda activate recenv &&  for deployment and start/B
                    GlobalVariables.PythonQueryFolderPath +" && " + "python SearchForQuery.py\"");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String readProcessOutput(InputStream is) {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            int c;
            String buffer = "";
            while ((c = isr.read()) >= 0) {
                buffer = buffer + (char) c;
            }
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String invokeOldQuery(String searchedKeysBulk, int nrOfResults) {
        ArrayList<String> command = new ArrayList<>(){
            {
                add("python");
                add(GlobalVariables.normalQueryPath);
                add(searchedKeysBulk);
                add(String.valueOf(nrOfResults));
            }
        };

        Process process = PythonService.getInstance().runCommand(command);
        String output = PythonService.getInstance().readProcessOutput(process.getInputStream());

        return output;
    }
}
