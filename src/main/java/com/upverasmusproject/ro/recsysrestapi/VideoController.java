package com.upverasmusproject.ro.recsysrestapi;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class VideoController {

    private final VideoRepository repo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    VideoController(VideoRepository repo) {

        RecsysRestapiApplication.setRepo(repo);
        this.repo = repo;
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
    String searchMethod(@RequestParam HashMap<String,Object> formData) {

        String searchedKeysBulk = (String)formData.get("keywords");
        if(searchedKeysBulk==null)
            return null;

        String[] searchedKeys = searchedKeysBulk.split(" ");
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

        List<Video> foundVideos = DatabaseSearcher.search(repo,DatabaseSearcher.processPythonQueryResult(queryResult.toString()));
        if(foundVideos!=null){
            try {
                Thread logQueryResult = new Thread(new QueryLogger(searchedKeysBulk, queryResult.toString()));
                logQueryResult.start();
                return DatabaseSearcher.writeListToJson(foundVideos);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return queryResult.toString();

    }

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
    }


}
