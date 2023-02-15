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

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import org.omnirom.device.utils.FileUtils;

public final class ButtonSwapPreference extends SwitchPreference implements
        Preference.OnPreferenceChangeListener {

    public static final String BUTTONS_SWAP_KEY = "buttons_swap";
    private static final String BUTTONS_SWAP_PATH = "/proc/touchpanel/reversed_keys_enable";
    private static final boolean BUTTONS_SWAP_DEFAULT_VALUE = false;

    public static final KernelFeature<Boolean> FEATURE = new KernelFeature<Boolean>() {

        @Override
        public boolean isSupported() {
            return FileUtils.isFileWritable(BUTTONS_SWAP_PATH);
        }

        @Override
        public Boolean getCurrentValue() {
            return FileUtils.getFileValueAsBoolean(BUTTONS_SWAP_PATH, false);
        }

        @Override
        public boolean applyValue(Boolean newValue) {
            return FileUtils.writeValue(BUTTONS_SWAP_PATH, newValue ? "1" : "0");
        }

        @Override
        public void applySharedPreferences(Boolean newValue, SharedPreferences sp) {
            sp.edit().putBoolean(BUTTONS_SWAP_KEY, newValue).apply();
        }

        @Override
        public boolean restore(SharedPreferences sp) {
            if(!isSupported()) return false;

            boolean value = sp.getBoolean(BUTTONS_SWAP_KEY, BUTTONS_SWAP_DEFAULT_VALUE);
            return applyValue(value);
        }
    };

    public ButtonSwapPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (FEATURE.isSupported()) {
            setOnPreferenceChangeListener(this);
        } else {
            setEnabled(false);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean value = (Boolean) newValue;
        if (FEATURE.applyValue(value))
            FEATURE.applySharedPreferences(value, getSharedPreferences());

        return true;
    }
}
