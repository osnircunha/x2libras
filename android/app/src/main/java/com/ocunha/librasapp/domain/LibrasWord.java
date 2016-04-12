package com.ocunha.librasapp.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by osnircunha on 4/7/16.
 */
public class LibrasWord implements Serializable{

    private String word;

    private String videoUrl;

    private String description;

    private String sample;

    private String librasSample;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getLibrasSample() {
        return librasSample;
    }

    public void setLibrasSample(String librasSample) {
        this.librasSample = librasSample;
    }

    @Override
    public String toString() {
        return this.getWord();
    }

    public String toJson(){
        JSONObject jsonObject= new JSONObject();
        try {
            for(Field f : this.getClass().getDeclaredFields()) {
                jsonObject.put(f.getName(), f.get(this));
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static LibrasWord fromJson(String json){
        LibrasWord word = new LibrasWord();
        try {
            JSONObject jsonObject   = new JSONObject(json);
            for(Field f : LibrasWord.class.getDeclaredFields()) {
                f.set(word, jsonObject.get(f.getName()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return word;
    }
}