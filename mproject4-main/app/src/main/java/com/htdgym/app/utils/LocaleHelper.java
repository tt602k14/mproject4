package com.htdgym.app.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "selected_language";

    public static Context setLocale(Context context, String languageCode) {
        persist(context, languageCode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, languageCode);
        }

        return updateResourcesLegacy(context, languageCode);
    }

    public static Context setLocale(Context context) {
        String languageCode = getPersistedLanguage(context);
        return setLocale(context, languageCode);
    }

    public static String getPersistedLanguage(Context context) {
        return SharedPrefManager.getInstance(context)
                .getSharedPreferences()
                .getString(SELECTED_LANGUAGE, "vi");
    }

    private static void persist(Context context, String languageCode) {
        SharedPrefManager.getInstance(context)
                .getSharedPreferences()
                .edit()
                .putString(SELECTED_LANGUAGE, languageCode)
                .apply();
    }

    private static Context updateResources(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public static String getLanguageName(String languageCode) {
        switch (languageCode) {
            case "vi": return "Tiếng Việt";
            case "en": return "English";
            case "fr": return "Français";
            case "de": return "Deutsch";
            default: return "Tiếng Việt";
        }
    }
}
