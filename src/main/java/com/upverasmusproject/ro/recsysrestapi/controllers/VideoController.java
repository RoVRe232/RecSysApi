package com.upverasmusproject.ro.recsysrestapi.controllers;


import com.upverasmusproject.ro.recsysrestapi.services.VideoService;
import com.upverasmusproject.ro.recsysrestapi.utils.exceptions.VideoNotFoundException;
import com.upverasmusproject.ro.recsysrestapi.entities.UserRepository;
import com.upverasmusproject.ro.recsysrestapi.entities.Video;
import com.upverasmusproject.ro.recsysrestapi.entities.VideoRepository;
import com.upverasmusproject.ro.recsysrestapi.services.PythonService;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;

@RestController
public class VideoController {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    VideoController(VideoRepository videoRepository, UserRepository userRepository) {
        VideoService.setVideosRepository(videoRepository);
        VideoService.setUserRepository(userRepository);
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/videos")
    List<Video> all(){
        return videoRepository.findAll();
    }

    @PostMapping("/videos")
    Video newVideo(@RequestBody Video newVideo){
        return videoRepository.save(newVideo);
    }

    @GetMapping("/videos/{id}")
    Video one(@PathVariable Long id){
        return  videoRepository.findById(id).orElseThrow(()->new VideoNotFoundException(id));
    }

    @PostMapping("/videos/{id}")
    Video replaceVideo(@RequestBody Video newVideo, @PathVariable Long id){
        return videoRepository.findById(id)
                .map(video -> {
                    video.setTitle(newVideo.getTitle());
                    video.setDate(newVideo.getDate());
                    return  videoRepository.save(video);
                }).orElseGet(()-> videoRepository.save(newVideo));
    }

    @DeleteMapping("/videos/{id}")
    void deleteVideo(@PathVariable Long id){
        videoRepository.deleteById(id);
    }

    @RequestMapping("/videos/search") //TODO improve search method
    String searchForVideo(@RequestParam HashMap<String,Object> formData) throws IOException {
        return VideoService.searchForVideo(formData);
    }

    @RequestMapping("/videos/oldsearch")
    String legacySearchForVideo(@RequestParam HashMap<String,Object> formData) {
        String searchedKeysBulk = (String)formData.get("keywords");
        if(searchedKeysBulk==null)
            return "Empty keys bulk";

        return PythonService.invokeOldQuery(searchedKeysBulk, 10);
    }
}
