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
    CommandLineRunner initDatabase(VideoRepository repo, UserRepository userRepo){
        ArrayList<String> keys = new ArrayList<>();
        keys.add("testkey1");
        keys.add("testkey2");
        return args -> {
            log.info("Preloading" + userRepo.save(new User("q2za-rtx5-4asd-rrrw", "John", "Visual",
                    new ArrayList<>())));
            log.info("Preloading"+ repo.save(new Video("0x36-3232-asdd-132e",
                    "VideoName", keys)));
            log.info("Preloading"+ repo.save(new Video("08adc2f5-c44a-f743-82d6-cb79b851f67f",
                    "Ley de Ohm. Resistencia electrica.", keys)));
            log.info("Preloading"+ repo.save(new Video("0x36-3232-asdd-14e",
                    "Sistema de sincronizacion con la red. Parte 3", keys)));
            log.info("Preloading"+ repo.save(new Video("0041e6b8-c12a-f647-a7ce-64e8482c6c26",
                    "El modelo de regresión simple I", keys)));
            log.info("Preloading"+ repo.save(new Video("04043f5b-ab82-c448-8dfb-5cf3eaf075bd",
                    "Rob�tica: componentes - Actuadores", keys)));
            log.info("Preloading"+ repo.save(new Video("c826d416-0d02-8046-80d7-4772d8ce67e3",
                    "Motor de cont�nua. Cotrol Digital. (II)", keys)));
            log.info("Preloading"+ repo.save(new Video("0x36-3232-asdd-14e",
                    "Transmisi�n", keys)));
            log.info("Preloading"+ repo.save(new Video("0a56e1fd-8dc8-b942-b9c2-781a796457d7",
                    "\"Teor�a de Circuitos\" - Teorema de Norton", keys)));
        };

    }



}
