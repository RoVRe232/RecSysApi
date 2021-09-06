package com.upverasmusproject.ro.recsysrestapi.services;

import com.upverasmusproject.ro.recsysrestapi.entities.*;
import com.upverasmusproject.ro.recsysrestapi.services.interfaces.ICommunicationService;
import com.upverasmusproject.ro.recsysrestapi.services.interfaces.IVideoService;
import com.upverasmusproject.ro.recsysrestapi.utils.database.DatabaseSearcher;
import com.upverasmusproject.ro.recsysrestapi.utils.logging.QueryLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class VideoService implements IVideoService {
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
    private static Process _queryProcessor;
    private static VideoRepository _videoRepository;
    private static UserRepository _userRepository;
    public static VideoService instance;

    private VideoService(){}

    static {
        instance = new VideoService();
        initializeVideoService();
    }

    private static void initializeVideoService()
    {
        try {
            CoreNLPService.getInstance().start();
            _queryProcessor = PythonService.getInstance().runSearchForQuery();
        }catch(Exception ex)
        {
            logger.error(ex.getMessage());
        }
    }

    public static String searchForVideo(HashMap<String,Object> formData) throws IOException {
        String keywords = (String)formData.get("keywords");
        if(keywords==null) return null;
        String queryResult = VideoService.instance.getRecSysQueryResults(keywords);
        List<VideoResult> videoResults = DatabaseSearcher.processPythonQueryResultToList(queryResult);

        String oldQueryResults = PythonService.invokeOldQuery(keywords, 10);

        try {
            //TODO add support for host ip for anonymous searches
            String userToken = (String)formData.get("usertoken");
            User currentUser = !(userToken==null || userToken.equals("")) ? _userRepository.findUserByToken(userToken) : null;

            Thread logQueryResult = new Thread(new QueryLogger(keywords,
                    DatabaseSearcher.writeListToJson(videoResults), currentUser,oldQueryResults));
            logQueryResult.start();

            List<Video> foundVideos = DatabaseSearcher.search(_videoRepository,DatabaseSearcher.processPythonQueryResult(queryResult));
            return DatabaseSearcher.writeListToJson(foundVideos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return DatabaseSearcher.writeListToJson(videoResults);
    }

    public String getRecSysQueryResults(String keywords)
    {
        try {
            ICommunicationService communicationService = new CommunicationService(_queryProcessor);
            communicationService.sendDataToProcess(keywords);
            return communicationService.awaitResponseFromProcess();
        } catch (IOException e) {
            e.printStackTrace();
            return "Query error: " + e.getMessage();
        }
    }

    public static void setVideosRepository(VideoRepository videosRepository){
        _videoRepository = videosRepository;}
    public static void setUserRepository(UserRepository userRepository){_userRepository = userRepository;}
    public static VideoRepository getVideosRepository(){return _videoRepository;}
}
