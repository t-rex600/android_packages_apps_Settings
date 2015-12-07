package com.android.settings.liquid;

import com.android.internal.logging.MetricsLogger;

import android.os.Bundle;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class LiquidSettings extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.liquid_settings);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.LIQUID_SETTINGS;
    }
}