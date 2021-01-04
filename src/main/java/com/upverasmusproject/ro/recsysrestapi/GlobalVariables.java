package com.upverasmusproject.ro.recsysrestapi;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

public final class GlobalVariables {
    public static final String CoreNLPPath =
            System.getProperty("core.nlp","D:\\MachineLearningSpania\\stanford-server");
    public static final String PythonQueryPath =
            System.getProperty("python.querypath" ,"D:\\MachineLearningSpania\\SearchForAQuery-master-v2\\SearchForQuery.py");
    public static final String videosUpvJson =
            System.getProperty("videos.upvjson" ,"D:\\MachineLearningSpania\\SearchForAQuery-master-v2\\videos_upv_cleaned.json");
    public static final String normalQueryPath =
            System.getProperty("normal.querypath", "D:\\MachineLearningSpania\\recsys-restapi (1)\\recsys-restapi\\resources\\scraper.py");
    public static final String PythonQueryFolderPath =
            System.getProperty("python.queryfolderpath", "D:\\MachineLearningSpania\\SearchForAQuery-master-v2");
    public static final String LogFile =
            System.getProperty("python.logfile", "D:\\MachineLearningSpania\\SearchForAQuery-master-v2\\queriesLog.txt");

}
