package com.upverasmusproject.ro.recsysrestapi;

import com.upverasmusproject.ro.recsysrestapi.services.VideoService;
import com.upverasmusproject.ro.recsysrestapi.utils.GlobalVariables;
import com.upverasmusproject.ro.recsysrestapi.utils.database.VideoJSONLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;

@SpringBootApplication
@EnableSwagger2
public class RecsysRestapiApplication {
	public static void main(String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "false");

		SpringApplication.run(RecsysRestapiApplication.class, args);

		Thread loadVideosJson = new Thread(new VideoJSONLoader(
				new File(GlobalVariables.videosUpvJson),
				VideoService.getVideosRepository()));
		loadVideosJson.start();
	}
}
