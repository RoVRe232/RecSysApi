package com.upverasmusproject.ro.recsysrestapi.services;

import com.upverasmusproject.ro.recsysrestapi.utils.GlobalVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CoreNLPService{
    private static final Logger log = LoggerFactory.getLogger(CoreNLPService.class);

    private static final CoreNLPService coreNLPService = new CoreNLPService();
    public static CoreNLPService getInstance(){
        return CoreNLPService.coreNLPService;
    }

    public void start() {
        try {
            if(SystemCheckService.portAvailable(9000))
                Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd "+ GlobalVariables.CoreNLPPath +" && " +
                        "java -Xmx4g -cp \"*\" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -serverProperties StanfordCoreNLP-spanish.properties -port 9000 -timeout 15000\"");
            else
                log.warn("Did not start a new instance of CoreNLP, port "+9000+" is already in use, maybe by CoreNLP or other app," +
                        "see if there is an open instance of CoreNLP cmd, if not, change port number to an available port");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
