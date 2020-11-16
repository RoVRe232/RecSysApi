package com.upverasmusproject.ro.recsysrestapi;

public class VideoNotFoundException extends RuntimeException {
    public VideoNotFoundException(Long id) {
        super("Could not find video "+id);
    }
}
