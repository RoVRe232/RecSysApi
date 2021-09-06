package com.upverasmusproject.ro.recsysrestapi.utils;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContext;
import java.util.Properties;

public final class GlobalVariables {

    public static final String absolutePath = System.getProperty("user.dir")+"\\resources\\";
    public static final String CoreNLPPath =
            System.getProperty("core.nlp",absolutePath+"core-nlp");
    public static final String PythonQueryPath =
            System.getProperty("python.querypath" ,absolutePath+"SearchForQuery.py");
    public static final String videosUpvJson =
            System.getProperty("videos.upvjson" ,absolutePath+"videos_upv_cleaned.json");
    public static final String normalQueryPath =
            System.getProperty("normal.querypath", absolutePath+"scraper.py");
    public static final String PythonQueryFolderPath =
            System.getProperty("python.queryfolderpath", absolutePath);
    public static final String LogFile =
            System.getProperty("python.logfile", absolutePath+"queriesLog.txt");

}
