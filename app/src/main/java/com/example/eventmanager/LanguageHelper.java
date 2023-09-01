package com.example.eventmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LanguageHelper {
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static void applyLanguage(Context context) {
        SharedPreferences shPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = shPreferences.getString(SELECTED_LANGUAGE, Locale.getDefault().getLanguage());

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }
}

