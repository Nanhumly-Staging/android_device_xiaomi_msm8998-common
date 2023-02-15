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

package org.omnirom.device.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import org.omnirom.device.utils.FileUtils;

public final class SweepToSleepPreference extends ListPreference implements
        Preference.OnPreferenceChangeListener {

    public static final String S2S_KEY = "sweep2sleep";
    private static final String S2S_DEFAULT = "0";
    private static final String FILE_S2S_TYPE = "/sys/sweep2sleep/sweep2sleep";

    public static final KernelFeature<String> FEATURE = new KernelFeature<String>() {

        @Override
        public boolean isSupported() {
            return FileUtils.isFileWritable(FILE_S2S_TYPE);
        }

        @Override
        public String getCurrentValue() {
            return FileUtils.getFileValue(FILE_S2S_TYPE, S2S_DEFAULT);
        }

        @Override
        public boolean applyValue(String newValue) {
            return FileUtils.writeValue(FILE_S2S_TYPE, newValue);
        }

        @Override
        public void applySharedPreferences(String newValue, SharedPreferences sp) {
            sp.edit().putString(S2S_KEY, newValue).apply();
        }

        @Override
        public boolean restore(SharedPreferences sp) {
            if (!isSupported()) return false;

            String value = sp.getString(S2S_KEY, S2S_DEFAULT);
            return applyValue(value);
        }
    };

    public SweepToSleepPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (FEATURE.isSupported()) {
            setOnPreferenceChangeListener(this);
        } else {
            setEnabled(false);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String strValue = newValue.toString();
        if (FEATURE.applyValue(strValue)) {
            FEATURE.applySharedPreferences(strValue, getSharedPreferences());
            notifyDependencyChange(false /* ignored */);
        }
        return true;
    }
    public static boolean isEnabled(SharedPreferences sp) {
        return !sp.getString(S2S_KEY, S2S_DEFAULT).equals(S2S_DEFAULT);
    }
}
