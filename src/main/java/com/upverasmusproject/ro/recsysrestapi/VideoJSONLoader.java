package com.upverasmusproject.ro.recsysrestapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class VideoJSONLoader implements Runnable {
    //TODO keywords are not loaded properly
    private static final Logger log = LoggerFactory.getLogger(VideoJSONLoader.class);
    private File videosUpvJson;
    private VideoRepository repo;

    public VideoJSONLoader(File videosUpvJson, VideoRepository repo) {
        this.videosUpvJson = videosUpvJson;
        this.repo = repo;
    }

    public void loadVideosJson(File videosUpvJson, VideoRepository repo) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(videosUpvJson);
        for(Iterator<JsonNode> it = root.elements(); it.hasNext();){
            JsonNode element=null,metadata=null,keywords=null, titleElement=null, idElement=null;
            String _id="", _title="", _keywords="";
            element = it.next();
            if(element!=null) {
                idElement = element.get("_id");
                if(idElement!=null)
                    _id = idElement.asText();
                titleElement = element.get("title");
                if(titleElement!=null)
                    _title = titleElement.asText();
                metadata = element.get("metadata");
            }
            if(metadata!= null)
                keywords = metadata.get("keywords");
            if(keywords!=null)
                _keywords = keywords.toString().replaceAll("\\[|\\]|\\'|\"","");

            log.info("Loading in db:" +
                    repo.save(new Video(_id, _title,new ArrayList<String>(Arrays.asList(_keywords.split(" "))))));
        }
    }


    @Override
    public void run() {
        try {
            loadVideosJson(videosUpvJson, repo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
