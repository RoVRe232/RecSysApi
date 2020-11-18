package com.upverasmusproject.ro.recsysrestapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String token;
    private String username, learningType;
    private ArrayList<String> queries;
    private static ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public User(){

    }

    public User(String token, String username, String learningType, ArrayList<String> queries) {
        this.token = token;
        this.username = username;
        this.learningType = learningType;
        this.queries = queries;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLearningType() {
        return learningType;
    }

    public void setLearningType(String learningType) {
        this.learningType = learningType;
    }

    public ArrayList<String> getQueries() {
        return queries;
    }

    public void setQueries(ArrayList<String> queries) {
        this.queries = queries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(token, user.token) &&
                Objects.equals(username, user.username) &&
                Objects.equals(learningType, user.learningType) &&
                Objects.equals(queries, user.queries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, username, learningType, queries);
    }

    @Override
    public String toString() {

        try {
            return User.objectWriter.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "User{" +
                "\"id\"=" + id +
                ", \"token\"=\"" + token + '\"' +
                ", \"username\"=\"" + username + '\"' +
                ", \"learningType\"=\"" + learningType + '\"' +
                ", \"queries\"=" + queries +
                '}';
    }
}
