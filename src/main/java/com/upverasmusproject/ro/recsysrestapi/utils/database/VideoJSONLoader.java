package com.upverasmusproject.ro.recsysrestapi.utils.database;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upverasmusproject.ro.recsysrestapi.entities.Video;
import com.upverasmusproject.ro.recsysrestapi.entities.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
            String _id="", _title="", _keywords="";

            JsonNode element = it.next();
            if(element == null || element.get("_id")==null || element.get("title")==null) {
                return;
            }

            _id = element.get("_id").asText();
            _title = element.get("title").asText();
            if(element.get("metadata")!=null){
                String unitedKeywords = it.next().get("metadata").get("keywords").asText();
                _keywords = unitedKeywords.toString().replaceAll("\\[|\\]|\\'|\"","");
            }

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
