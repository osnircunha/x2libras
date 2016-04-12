package com.ocunha.librasapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ocunha.librasapp.LibrasApplication;
import com.ocunha.librasapp.domain.LibrasWord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by osnircunha on 4/10/16.
 */
public class Preferences {
    private static Preferences INSTANCE;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private Preferences(){ }

    public static Preferences getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new Preferences();
            INSTANCE.settings = LibrasApplication.getContext().getSharedPreferences("LIBRAS_PREFERENCES", Context.MODE_PRIVATE);
            INSTANCE.editor = INSTANCE.settings.edit();
        }
        return INSTANCE;
    }

    public void setWordList(String words){
        this.editor.putString("words", words).commit();
    }

    public ArrayList<LibrasWord> getWordList(){
        return JsonUtils.parseJsonListToLibrasWord(this.settings.getString("words", null));
    }

    public void logSearchHistory(LibrasWord word){
        ArrayList<LibrasWord> history = this.getSearchHistory();
        history.add(word);

        Set<String> set = new HashSet<>();
        for(LibrasWord librasWord : history){
            set.add(librasWord.toJson());
        }

        this.editor.putStringSet("search_history", set).commit();
    }

    public ArrayList<LibrasWord> getSearchHistory(){
        ArrayList<LibrasWord> words = new ArrayList<>();

        for(String s : this.settings.getStringSet("search_history", new HashSet<String>())){
            words.add(LibrasWord.fromJson(s));
        }

        return words;
    }

}
