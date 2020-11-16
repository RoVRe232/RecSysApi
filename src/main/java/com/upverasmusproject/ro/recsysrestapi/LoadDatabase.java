package com.upverasmusproject.ro.recsysrestapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(VideoRepository repo){
        ArrayList<String> keys = new ArrayList<>();
        keys.add("testkey1");
        keys.add("testkey2");
        return args -> {
            log.info("Preloading"+ repo.save(new Video("0x36-3232-asdd-132e",
                    "VideoName", keys)));
            log.info("Preloading"+ repo.save(new Video("0x26-7432-asdd-132e",
                    "Ley de Ohm. Resistencia electrica.", keys)));
            log.info("Preloading"+ repo.save(new Video("0x36-3232-asdd-14e",
                    "Sistema de sincronizacion con la red. Parte 3", keys)));
        };

    }


}
