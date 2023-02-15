/*
 * Copyright (C) 2019 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.omnirom.device;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceScreen;

public final class DeviceSettings extends PreferenceFragment {

    private static final String KEY_CATEGORY_DISPLAY = "display";
    private static final String KEY_CATEGORY_KCAL = "kcal";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.device_settings, rootKey);

        PreferenceScreen prefSet = getPreferenceScreen();

        findPreference(KEY_CATEGORY_KCAL).setEnabled(DisplayCalibration.isSupported());
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (KEY_CATEGORY_KCAL.equals(preference.getKey())) {
            DisplayCalibrationActivity.startActivity(getContext());
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }
}