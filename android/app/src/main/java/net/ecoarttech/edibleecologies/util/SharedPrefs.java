package net.ecoarttech.edibleecologies.util;

import android.content.Context;
import android.content.SharedPreferences;

import net.ecoarttech.edibleecologies.App;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pkoronkevich on 4/10/15.
 */
public class SharedPrefs {

    private static final String PREFS_FILE = "prefs";

    private static final String PREFS_KEY_QUESTIONS = "questions";

    private static SharedPreferences getPrefs(){
        return App.getContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    public static void initDefaultQuestions(){
        if (getQuestions() == null){
            ArrayList<String> defaultQuestions = new ArrayList<>();
            defaultQuestions.add("Default Question just in case?");
            saveQuestions(defaultQuestions);
        }
    }

    public static void saveQuestions(List<String> questions){
        SharedPreferences prefs = getPrefs();
        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet(PREFS_KEY_QUESTIONS, new HashSet<>(questions));
        edit.commit();
    }

    public static List<String> getQuestions(){
        SharedPreferences prefs = getPrefs();
        Set<String> set = prefs.getStringSet(PREFS_KEY_QUESTIONS, null);
        if (set != null)
            return new ArrayList(set);
        return null;
    }
}
