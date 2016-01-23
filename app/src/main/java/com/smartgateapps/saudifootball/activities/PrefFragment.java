package com.smartgateapps.saudifootball.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.smartgateapps.saudifootball.R;
import com.smartgateapps.saudifootball.model.Legue;
import com.smartgateapps.saudifootball.saudi.MyApplication;

import java.util.List;

/**
 * Created by Raafat on 12/01/2016.
 */
public class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private List<Legue> allLegues;

    private SwitchPreference abdAlatifPref,
            waliAlahidPref,
            khadimAlhrmPref,
            firstClassPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


        abdAlatifPref = (SwitchPreference) findPreference(getString(R.string.abd_alatif_notificatin_pref_key));
        waliAlahidPref = (SwitchPreference) findPreference(getString(R.string.wali_notification_pref_key));
        khadimAlhrmPref = (SwitchPreference) findPreference(getString(R.string.khadim_notification_pref_key));
        firstClassPref = (SwitchPreference) findPreference(getString(R.string.first_notification_pref_key));

        boolean abdAlatifNotification = MyApplication.pref.getBoolean(getString(R.string.abd_alatif_notificatin_pref_key), false);
        boolean waliAlahidNotificatin = MyApplication.pref.getBoolean(getString(R.string.wali_notification_pref_key), false);
        boolean khadimAlhrmNotification = MyApplication.pref.getBoolean(getString(R.string.khadim_notification_pref_key), false);
        boolean firstClassNotification = MyApplication.pref.getBoolean(getString(R.string.first_class_dawri_menu_item), false);

        abdAlatifPref.setChecked(abdAlatifNotification);
        waliAlahidPref.setChecked(waliAlahidNotificatin);
        khadimAlhrmPref.setChecked(khadimAlhrmNotification);
        firstClassPref.setChecked(firstClassNotification);

    }

    @Override
    public void onResume() {
        super.onResume();

        abdAlatifPref.setOnPreferenceChangeListener(this);
        waliAlahidPref.setOnPreferenceChangeListener(this);
        khadimAlhrmPref.setOnPreferenceChangeListener(this);
        firstClassPref.setOnPreferenceChangeListener(this);

        MyApplication.pref.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        MyApplication.pref.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String key = preference.getKey();
        if (preference instanceof SwitchPreference) {
            boolean value = (boolean) newValue;

            if (key.equalsIgnoreCase(getString(R.string.abd_alatif_notificatin_pref_key))) {
                MyApplication
                        .pref.
                        edit()
                        .putBoolean(getString(R.string.abd_alatif_notificatin_pref_key), value)
                        .apply();

            } else if (key.equalsIgnoreCase(getString(R.string.wali_notification_pref_key))) {
                MyApplication
                        .pref.
                        edit()
                        .putBoolean(getString(R.string.wali_notification_pref_key), value)
                        .apply();

            } else if (key.equalsIgnoreCase(getString(R.string.khadim_notification_pref_key))) {
                MyApplication
                        .pref.
                        edit()
                        .putBoolean(getString(R.string.khadim_notification_pref_key), value)
                        .apply();

            } else if (key.equalsIgnoreCase(getString(R.string.first_notification_pref_key))) {
                MyApplication
                        .pref.
                        edit()
                        .putBoolean(getString(R.string.first_notification_pref_key), value)
                        .apply();
            }
            return true;

        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        boolean abdAlatifNotification = MyApplication.pref.getBoolean(getString(R.string.abd_alatif_notificatin_pref_key), false);
        boolean waliAlahidNotificatin = MyApplication.pref.getBoolean(getString(R.string.wali_notification_pref_key), false);
        boolean khadimAlhrmNotification = MyApplication.pref.getBoolean(getString(R.string.khadim_notification_pref_key), false);
        boolean firstClassNotification = MyApplication.pref.getBoolean(getString(R.string.first_class_dawri_menu_item), false);

        if (key.equalsIgnoreCase(getString(R.string.abd_alatif_notificatin_pref_key))) {
            abdAlatifPref.setChecked(abdAlatifNotification);

        } else if (key.equalsIgnoreCase(getString(R.string.wali_notification_pref_key))) {
            waliAlahidPref.setChecked(waliAlahidNotificatin);

        } else if (key.equalsIgnoreCase(getString(R.string.khadim_notification_pref_key))) {
            khadimAlhrmPref.setChecked(khadimAlhrmNotification);

        } else if (key.equalsIgnoreCase(getString(R.string.first_notification_pref_key))) {
            firstClassPref.setChecked(firstClassNotification);

        }
    }
}


