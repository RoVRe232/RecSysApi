package com.upverasmusproject.ro.recsysrestapi;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.python.core.PyInstance;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class VideoController {

    private final VideoRepository repo;
    private final UserRepository userRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    VideoController(VideoRepository repo, UserRepository userRepo) {

        RecsysRestapiApplication.setRepo(repo);
        RecsysRestapiApplication.setUserRepo(userRepo);
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public VideoRepository getRepo(){
        return repo;
    }

    @GetMapping("/videos")
    List<Video> all(){
        return repo.findAll();
    }

    @PostMapping("/videos")
    Video newVideo(@RequestBody Video newVideo){
        return repo.save(newVideo);
    }

    @GetMapping("/videos/{id}")
    Video one(@PathVariable Long id){
        return  repo.findById(id).orElseThrow(()->new VideoNotFoundException(id));
    }

    @PostMapping("/videos/{id}")
    Video replaceVideo(@RequestBody Video newVideo, @PathVariable Long id){

        return repo.findById(id)
                .map(video -> {
                    video.setTitle(newVideo.getTitle());
                    video.setDate(newVideo.getDate());
                    return  repo.save(video);
                }).orElseGet(()->{
//                    newVideo.setId(id);
                    return  repo.save(newVideo);
                });
    }

    @DeleteMapping("/videos/{id}")
    void deleteVideo(@PathVariable Long id){
        repo.deleteById(id);
    }

    @RequestMapping("/search") //TODO improve search method
    String searchMethod(@RequestParam HashMap<String,Object> formData) throws IOException {
        User currentUser = null;
        String userToken = (String)formData.get("usertoken");
        if(!(userToken==null || userToken.equals(""))){
             currentUser = userRepo.findUserByToken(userToken);
        }

        String searchedKeysBulk = (String)formData.get("keywords");
        if(searchedKeysBulk==null)
            return null;

        System.out.println(searchedKeysBulk);

        OutputStreamWriter queryWriter = RecsysRestapiApplication.getQueryProcessorWriter();
        try {
            queryWriter.write(searchedKeysBulk+'\n');
            queryWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder queryResult = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        BufferedReader queryReader = RecsysRestapiApplication.getQueryProcessorReader();
        Thread errThread = new Thread(RecsysRestapiApplication.getQueryProcessorErrGobbler());
        errThread.start();
        String line = null;
        try{
            //TODO add support for spanish characters
            while((line=queryReader.readLine())!= null){
                System.out.println("Appended line: "+line);
                sb.append(line);
                if(line.startsWith("("))
                    queryResult.append(line+'\n');
                sb.append(System.getProperty("line.separator"));
                if(line.equals("eoq"))
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            errThread.interrupt();
        }
        String oldQueryResults = PythonRunner.invokeOldQuery(searchedKeysBulk, 10);
        System.out.println("old query results: "+ oldQueryResults);
        List<VideoResult> videoResults = DatabaseSearcher.processPythonQueryResultToList(queryResult.toString());
        List<Video> foundVideos = DatabaseSearcher.search(repo,DatabaseSearcher.processPythonQueryResult(queryResult.toString()));
        try {
            Thread logQueryResult = new Thread(new QueryLogger(searchedKeysBulk,
                    DatabaseSearcher.writeListToJson(videoResults), currentUser,oldQueryResults));
            logQueryResult.start();
            return DatabaseSearcher.writeListToJson(foundVideos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return DatabaseSearcher.writeListToJson(videoResults);

    }


    @RequestMapping("/oldsearch") //TODO improve search method
    String oldSearchMethod(@RequestParam HashMap<String,Object> formData) throws IOException {
        User currentUser = null;
        String userToken = (String)formData.get("usertoken");
        if(!(userToken==null || userToken.equals(""))){
            currentUser = userRepo.findUserByToken(userToken);
        }

        String searchedKeysBulk = (String)formData.get("keywords");
        if(searchedKeysBulk==null)
            return "Empty keys bulk";

        return PythonRunner.invokeOldQuery(searchedKeysBulk, 10);
    }
/*
    @GetMapping("/callpython")
    OutputStreamWriter callPython(){
        PythonRunner pythonRunner = new PythonRunner();
        ArrayList<String> cmd = pythonRunner.buildSimplePythonRunnerCommand("D:\\MachineLearningSpania\\SearchForAQuery-master-v2\\SearchForQuery.py");
        Process proc = pythonRunner.runCommand(cmd);
        OutputStream procOutputStream = proc.getOutputStream();
        OutputStreamWriter procOSwriter = new OutputStreamWriter(procOutputStream);
        return procOSwriter;
    }

    @GetMapping("/startcnlp")
    String startCNLP(){
        CoreNLPStarter cnlpStarter = new CoreNLPStarter();
        cnlpStarter.run();
        return "Started cnlp server!";
    }*/


}
