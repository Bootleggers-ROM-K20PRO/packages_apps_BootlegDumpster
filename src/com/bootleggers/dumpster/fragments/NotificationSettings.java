package com.bootleggers.dumpster.fragments;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.Vibrator;
import androidx.preference.PreferenceCategory;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;

import com.android.settings.SettingsPreferenceFragment;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
import com.bootleggers.dumpster.preferences.SystemSettingMasterSwitchPreference;
import com.bootleggers.dumpster.preferences.CustomSeekBarPreference;

import com.android.internal.logging.nano.MetricsProto;

public class NotificationSettings extends SettingsPreferenceFragment
    implements Preference.OnPreferenceChangeListener {

    private PreferenceCategory mLedsCategory;
    private Preference mChargingLeds;
    private SystemSettingMasterSwitchPreference mEdgePulse;

    private static final String PULSE_AMBIENT_LIGHT = "pulse_ambient_light";



    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.bootleg_dumpster_notifications);
        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefSet = getPreferenceScreen();

        mLedsCategory = (PreferenceCategory) findPreference("light_category");
        mChargingLeds = (Preference) findPreference("battery_charging_light");
        if (mChargingLeds != null
                && !getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveBatteryLed)) {
            mLedsCategory.removePreference(mChargingLeds);
        }
          if (mChargingLeds == null) {
            prefSet.removePreference(mLedsCategory);
        }

        mEdgePulse = (SystemSettingMasterSwitchPreference) findPreference(PULSE_AMBIENT_LIGHT);
        mEdgePulse.setOnPreferenceChangeListener(this);
        int edgePulse = Settings.System.getInt(getContentResolver(),
                PULSE_AMBIENT_LIGHT, 0);
        mEdgePulse.setChecked(edgePulse != 0);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
            if (preference == mEdgePulse) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
    	                  PULSE_AMBIENT_LIGHT, value ? 1 : 0);
            return true;
        }
        switch (preference.getKey()) {
            default:
        return false;
       }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BOOTLEG;
    }
}
