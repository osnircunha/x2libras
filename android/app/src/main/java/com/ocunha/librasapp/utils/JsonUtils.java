package com.ocunha.librasapp.utils;

import com.ocunha.librasapp.domain.LibrasWord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by osnircunha on 4/10/16.
 */
public class JsonUtils {

    private JsonUtils(){}

    public static ArrayList<LibrasWord> parseJsonListToLibrasWord(String wordList){
        ArrayList<LibrasWord> librasWords = new ArrayList<>();
        if(wordList == null){
            return null;
        }

        try {
            JSONArray array = new JSONArray(wordList);

            for (int i = 0; i < array.length(); i++) {
                if (!array.isNull(i)) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    LibrasWord item = new LibrasWord();
                    item.setWord(jsonObject.getString("word"));
                    item.setDescription(jsonObject.getString("description"));
                    item.setLibrasSample(jsonObject.getString("librasSample"));
                    item.setSample(jsonObject.getString("sample"));
                    item.setVideoUrl(jsonObject.getString("video"));

                    librasWords.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return librasWords;
    }
}
