package com.android.settings.liquid;

import com.android.internal.logging.MetricsLogger;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import java.util.Date;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class LiquidAbout extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.liquid_about);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
    ContentResolver resolver = getActivity().getContentResolver();
	return false;
	}

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.ABOUT_LIQUID;
    }
}