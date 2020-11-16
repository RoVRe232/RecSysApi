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
            JsonNode element = it.next();
            String _id = element.get("_id").asText();
            String _title = element.get("title").asText();
            JsonNode metadata = element.get("metadata");
            String _keywords = metadata.get("keywords").asText();

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
