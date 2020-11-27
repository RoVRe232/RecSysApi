package com.upverasmusproject.ro.recsysrestapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.*;

@SpringBootApplication
public class RecsysRestapiApplication {
	private static final Logger log = LoggerFactory.getLogger(RecsysRestapiApplication.class);
	private static final Process queryProcessor = PythonRunner.getInstance().runCommand(
			PythonRunner.getInstance().buildSimplePythonRunnerCommand(GlobalVariables.PythonQueryPath));
	private static final OutputStreamWriter queryProcessorWriter = new OutputStreamWriter(queryProcessor.getOutputStream());
	private static final BufferedReader queryProcessorReader = new BufferedReader(
			new InputStreamReader(queryProcessor.getInputStream()));
	private static final StreamGobbler queryProcessorErrGobbler = new StreamGobbler(queryProcessor.getErrorStream(), "ERROR");
    private static VideoRepository repo = null;
    private static UserRepository userRepository = null;
	public static void main(String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "false");
		Checker.doChecks();

		Thread coreNLPThread = new Thread(new CoreNLPStarter());
		coreNLPThread.start();
//		CoreNLPStarter coreNLPStarter = new CoreNLPStarter();
//		coreNLPStarter.run();

		log.info("Started Stanford - CoreNLP");


		
		SpringApplication.run(RecsysRestapiApplication.class, args);

		Thread loadVideosJson = new Thread(new VideoJSONLoader(
				new File(GlobalVariables.videosUpvJson),
				repo));
		loadVideosJson.start();

	}

	public static final Process getQueryProcessor(){
		return queryProcessor;
	}
	public static final OutputStreamWriter getQueryProcessorWriter(){return queryProcessorWriter;}
	public static final BufferedReader getQueryProcessorReader(){return queryProcessorReader;}
	public static final StreamGobbler getQueryProcessorErrGobbler(){return queryProcessorErrGobbler;}

	public static void setRepo(VideoRepository repo) {
		RecsysRestapiApplication.repo = repo;
	}

	public static void setUserRepo(UserRepository userRepo) {
		RecsysRestapiApplication.userRepository = userRepo;
	}
}
