package com.upverasmusproject.ro.recsysrestapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Objects;

@Entity
public class Video {
    @Id
    @GeneratedValue
    private Long dbid;
    private String id;
    private String title;
    private String date;
    private ArrayList<String> keywords;
    private static ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    public  Video(){};

    public Video(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public Video(String id, String title, ArrayList<String> keywords) {
         this.id = id;
         this.title = title;
         this.keywords = keywords;
    }

    public String getId() {return id;}

    public void setId(String id){this.id = id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object o){
        if(this==o)
            return true;
        if(!(o instanceof  Video))
            return false;
        return id.equals(((Video) o).id) && id.equals(((Video) o).title);

    }

    @Override
    public int hashCode(){
        return Objects.hash(id, title);
    }

    @Override
    public String toString(){

        try {
            return Video.objectWriter.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  "Video{"+"id="+id+", title='"+title+"', keywords='none'";//if jackson fails
    }


}
