/*
* Copyright (C) 2015 Team Horizon
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.android.settings;

import android.os.Bundle;

import com.android.internal.logging.MetricsLogger;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.WindowManagerGlobal;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class ButtonSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

	private static final String VOLUME_KEY_CURSOR_CONTROL = "volume_key_cursor_control";
	private static final String VOLUME_ROCKER_WAKE = "volume_rocker_wake";
	private static final String KEY_VOLUME_CONTROL_RING_STREAM = "volume_keys_control_ring_stream";
        private static final String NAVIGATION_BAR_TINT = "navigation_bar_tint";

	private ListPreference mVolumeKeyCursorControl;
	private SwitchPreference mVolumeRockerWake;
	private SwitchPreference mVolumeControlRingStream;
        private ColorPickerPreference mNavbarButtonTint;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.button_settings);

        // Navigation bar button color
        mNavbarButtonTint = (ColorPickerPreference) findPreference(NAVIGATION_BAR_TINT);
        mNavbarButtonTint.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.NAVIGATION_BAR_TINT, 0xffffffff);
        String hexColor = String.format("#%08x", (0xffffffff & intColor));
        mNavbarButtonTint.setSummary(hexColor);
        mNavbarButtonTint.setNewPreviewColor(intColor);

		// volume key cursor control
        mVolumeKeyCursorControl = (ListPreference) findPreference(VOLUME_KEY_CURSOR_CONTROL);
        if (mVolumeKeyCursorControl != null) {
            mVolumeKeyCursorControl.setOnPreferenceChangeListener(this);
            int volumeRockerCursorControl = Settings.System.getInt(getContentResolver(),
                    Settings.System.VOLUME_KEY_CURSOR_CONTROL, 0);
            mVolumeKeyCursorControl.setValue(Integer.toString(volumeRockerCursorControl));
            mVolumeKeyCursorControl.setSummary(mVolumeKeyCursorControl.getEntry());
        }

		mVolumeRockerWake = (SwitchPreference) findPreference(VOLUME_ROCKER_WAKE);
        mVolumeRockerWake.setOnPreferenceChangeListener(this);
        int volumeRockerWake = Settings.System.getInt(getContentResolver(),
                VOLUME_ROCKER_WAKE, 0);
        mVolumeRockerWake.setChecked(volumeRockerWake != 0);

		mVolumeControlRingStream = (SwitchPreference)
                findPreference(KEY_VOLUME_CONTROL_RING_STREAM);
        int volumeControlRingtone = Settings.System.getInt(getContentResolver(),
                Settings.System.VOLUME_KEYS_CONTROL_RING_STREAM, 1);
        if (mVolumeControlRingStream != null) {
            mVolumeControlRingStream.setChecked(volumeControlRingtone > 0);
        }
    }

	private void handleActionListChange(ListPreference pref, Object newValue, String setting) {
        String value = (String) newValue;
        int index = pref.findIndexOfValue(value);
        pref.setSummary(pref.getEntries()[index]);
        Settings.System.putInt(getContentResolver(), setting, Integer.valueOf(value));
    }

	@Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mVolumeKeyCursorControl) {
            handleActionListChange(mVolumeKeyCursorControl, newValue,
                    Settings.System.VOLUME_KEY_CURSOR_CONTROL);
            return true;
		} else if (preference == mVolumeRockerWake) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.VOLUME_ROCKER_WAKE,
                    ((Boolean) newValue) ? 1 : 0);
        } else if (preference == mNavbarButtonTint) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_TINT, intHex);
            return true;
        }
        return false;
    }

	@Override
    protected int getMetricsCategory() {
        return MetricsLogger.APPLICATION;
    }

	@Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mVolumeControlRingStream) {
            int value = mVolumeControlRingStream.isChecked() ? 1 : 0;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.VOLUME_KEYS_CONTROL_RING_STREAM, value);
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

}
