package com.example.newentryclear;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.newentryclear.R;

public class Preferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.xml.activity_preferences);
        addPreferencesFromResource(R.xml.preferences);
    }
}
