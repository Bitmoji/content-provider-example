package com.bitstrips.example.contentproviderdemo.model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

public enum Language {

    DEFAULT("Default", null),
    ENGLISH("English", "en"),
    FRENCH("French", "fr-FR"),
    SPANISH("Spanish", "es"),
    GERMAN("German", "de"),
    PORTUGUESE("Portuguese (Brazil)", "pt-BR"),
    KOREAN("Korean", "ko"),
    JAPANESE("Japanese", "ja");

    final String mDisplayName;
    final String mLocaleString;

    Language(String displayName, String localeString) {
        mDisplayName = displayName;
        mLocaleString = localeString;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    @Nullable
    public String getLocaleString() {
        return mLocaleString;
    }

    public static ArrayAdapter<String> asArrayAdapter(Context context) {
        String[] titles = new String[values().length];

        for (int i = 0; i < values().length; i++) {
            titles[i] = values()[i].getDisplayName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, titles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
