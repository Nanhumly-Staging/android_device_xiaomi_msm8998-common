/*
 * Copyright (C) 2016 The OmniROM Project
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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import org.omnirom.device.Preference.KcalSeekBarPreference;
import org.omnirom.device.utils.FileUtils;
import org.omnirom.device.utils.UtilsKCAL;

import static org.omnirom.device.utils.FileUtils.isFileWritable;

public final class DisplayCalibration extends PreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "DisplayCalibration";

    public static final String KEY_KCAL_ENABLED = "kcal_enabled";
    public static final String KEY_KCAL_RED = "kcal_red";
    public static final String KEY_KCAL_GREEN = "kcal_green";
    public static final String KEY_KCAL_BLUE = "kcal_blue";
    public static final String KEY_KCAL_SATURATION = "kcal_saturation";
    public static final String KEY_KCAL_CONTRAST = "kcal_contrast";
    public static final String KEY_KCAL_COLOR_TEMP = "kcal_color_temp";

    private KcalSeekBarPreference mKcalRed;
    private KcalSeekBarPreference mKcalBlue;
    private KcalSeekBarPreference mKcalGreen;
    private KcalSeekBarPreference mKcalSaturation;
    private KcalSeekBarPreference mKcalContrast;
    private KcalSeekBarPreference mKcalColorTemp;
    private SharedPreferences mPrefs;
    private SwitchPreference mKcalEnabled;

    private String mRed;
    private String mBlue;
    private String mGreen;

    private static final String COLOR_FILE = "/sys/devices/platform/kcal_ctrl.0/kcal";
    private static final String COLOR_FILE_CONTRAST = "/sys/devices/platform/kcal_ctrl.0/kcal_cont";
    private static final String COLOR_FILE_SATURATION = "/sys/devices/platform/kcal_ctrl.0/kcal_sat";
    private static final String COLOR_FILE_ENABLE = "/sys/devices/platform/kcal_ctrl.0/kcal_enable";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        addPreferencesFromResource(R.xml.display_calibration);

        mKcalEnabled = findPreference(KEY_KCAL_ENABLED);
        mKcalEnabled.setChecked(mPrefs.getBoolean(DisplayCalibration.KEY_KCAL_ENABLED, false));
        mKcalEnabled.setOnPreferenceChangeListener(this);
        mKcalEnabled.setIconSpaceReserved(false);

        mKcalRed = findPreference(KEY_KCAL_RED);
        mKcalRed.setValue(mPrefs.getInt(KEY_KCAL_RED, mKcalRed.getDefault()));
        mKcalRed.setOnPreferenceChangeListener(this);

        mKcalGreen = findPreference(KEY_KCAL_GREEN);
        mKcalGreen.setProgress(mPrefs.getInt(KEY_KCAL_GREEN, mKcalGreen.getDefault()));
        mKcalGreen.setOnPreferenceChangeListener(this);

        mKcalBlue = findPreference(KEY_KCAL_BLUE);
        mKcalBlue.setProgress(mPrefs.getInt(KEY_KCAL_BLUE, mKcalBlue.getDefault()));
        mKcalBlue.setOnPreferenceChangeListener(this);

        mKcalSaturation = findPreference(KEY_KCAL_SATURATION);
        mKcalSaturation.setProgress(mPrefs.getInt(KEY_KCAL_SATURATION, mKcalSaturation.getDefault()));
        mKcalSaturation.setOnPreferenceChangeListener(this);

        mKcalContrast = findPreference(KEY_KCAL_CONTRAST);
        mKcalContrast.setProgress(mPrefs.getInt(KEY_KCAL_CONTRAST, mKcalContrast.getDefault()));
        mKcalContrast.setOnPreferenceChangeListener(this);

        mKcalColorTemp = findPreference(KEY_KCAL_COLOR_TEMP);
        mKcalColorTemp.setProgress(mPrefs.getInt(KEY_KCAL_COLOR_TEMP, mKcalColorTemp.getDefault()));
        mKcalColorTemp.setOnPreferenceChangeListener(this);

        mRed = String.valueOf(mPrefs.getInt(KEY_KCAL_RED, mKcalRed.getDefault()));
        mGreen = String.valueOf(mPrefs.getInt(KEY_KCAL_GREEN, mKcalGreen.getDefault()));
        mBlue = String.valueOf(mPrefs.getInt(KEY_KCAL_BLUE, mKcalBlue.getDefault()));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // override popup view for kcal temperature
        mKcalColorTemp.setPopupWidth(R.dimen.seek_bar_popup_text_medium);
    }

    static boolean isSupported() {
        return isFileWritable(COLOR_FILE_ENABLE) && isFileWritable(COLOR_FILE);
    }

    static void restore(SharedPreferences sp) {
        boolean storeEnabled = sp.getBoolean(DisplayCalibration.KEY_KCAL_ENABLED, false);
        Log.d(TAG, "onRestore: isEnabled: " + storeEnabled);
        if (!isSupported()) {
            Log.e(TAG, "onRestore: Access denied: " + COLOR_FILE);
            return;
        }

        if (storeEnabled) {
            FileUtils.writeValue(COLOR_FILE_ENABLE, "1");
            FileUtils.writeValue(COLOR_FILE, "1");
            int storedRed = sp.getInt(DisplayCalibration.KEY_KCAL_RED, 256);
            int storedGreen = sp.getInt(DisplayCalibration.KEY_KCAL_GREEN, 256);
            int storedBlue = sp.getInt(DisplayCalibration.KEY_KCAL_BLUE, 256);
            int storedSaturation = sp.getInt(DisplayCalibration.KEY_KCAL_SATURATION, 255);
            int storedContrast = sp.getInt(DisplayCalibration.KEY_KCAL_CONTRAST, 255);

            Log.d(TAG, "onRestore: R: " + storedRed + ", G: " + storedGreen + ",B: " + storedBlue);
            Log.d(TAG, "onRestore: Sa: " + storedSaturation +", contrast: " + storedContrast);
            FileUtils.writeValue(COLOR_FILE, storedRed + " " + storedGreen + " " + storedBlue);
            FileUtils.writeValue(COLOR_FILE_CONTRAST, String.valueOf(storedContrast));
            FileUtils.writeValue(COLOR_FILE_SATURATION, String.valueOf(storedSaturation));
        }
    }

    void reset() {
        int red = mKcalRed.reset();
        int green = mKcalGreen.reset();
        int blue = mKcalBlue.reset();
        int saturation = mKcalSaturation.reset();
        int contrast = mKcalContrast.reset();

        mPrefs.edit().putInt(KEY_KCAL_RED, red)
                .putInt(KEY_KCAL_GREEN, green)
                .putInt(KEY_KCAL_BLUE, blue)
                .putInt(KEY_KCAL_SATURATION, saturation)
                .putInt(KEY_KCAL_CONTRAST, contrast)
                .apply();

        String storedValue = red + " " + green + " " + blue;

        FileUtils.writeValue(COLOR_FILE, storedValue);
        FileUtils.writeValue(COLOR_FILE_SATURATION, Integer.toString(saturation));
        FileUtils.writeValue(COLOR_FILE_CONTRAST, Integer.toString(contrast));

        int cct = UtilsKCAL.KfromRGB(mPrefs.getInt(KEY_KCAL_RED, 256), mPrefs.getInt(KEY_KCAL_GREEN, 256), mPrefs.getInt(KEY_KCAL_BLUE, 256));
        mKcalColorTemp.setValue(cct);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        switch (key) {
            case KEY_KCAL_RED:
            case KEY_KCAL_GREEN:
            case KEY_KCAL_BLUE:
                return updateRGB(key, (Integer) newValue);
            case KEY_KCAL_COLOR_TEMP:
                int color = Integer.parseInt(newValue.toString());
                int[] colorTemp = UtilsKCAL.RGBfromK(color);
                int red = colorTemp[0];
                int green = colorTemp[1];
                int blue = colorTemp[2];

                mKcalRed.setValue(red);
                mKcalGreen.setValue(green);
                mKcalBlue.setValue(blue);

                mPrefs.edit().putInt(KEY_KCAL_RED, red)
                        .putInt(KEY_KCAL_GREEN, green)
                        .putInt(KEY_KCAL_BLUE, blue)
                        .apply();

                FileUtils.writeValue(COLOR_FILE, red + " " + green + " " + blue);
                return true;
            case KEY_KCAL_ENABLED:
                Boolean enabled = (Boolean) newValue;
                mPrefs.edit().putBoolean(KEY_KCAL_ENABLED, enabled).apply();
                mRed = String.valueOf(mPrefs.getInt(KEY_KCAL_RED, 256));
                mBlue = String.valueOf(mPrefs.getInt(KEY_KCAL_BLUE, 256));
                mGreen = String.valueOf(mPrefs.getInt(KEY_KCAL_GREEN, 256));
                String storedValue = (mRed + " " + mGreen + " " + mBlue);
                String mSaturation = String.valueOf(mPrefs.getInt(KEY_KCAL_SATURATION, 256));
                String mContrast = String.valueOf(mPrefs.getInt(KEY_KCAL_CONTRAST, 256));
                FileUtils.writeValue(COLOR_FILE_ENABLE, enabled ? "1" : "0");
                FileUtils.writeValue(COLOR_FILE, storedValue);
                FileUtils.writeValue(COLOR_FILE_SATURATION, mSaturation);
                FileUtils.writeValue(COLOR_FILE_CONTRAST, mContrast);

                int cct = UtilsKCAL.KfromRGB(mPrefs.getInt(KEY_KCAL_RED, 256), mPrefs.getInt(KEY_KCAL_GREEN, 256), mPrefs.getInt(KEY_KCAL_BLUE, 256));
                mKcalColorTemp.setValue(cct);
                return true;
            case KEY_KCAL_SATURATION:
                mPrefs.edit().putInt(KEY_KCAL_SATURATION, (int) newValue).apply();
                FileUtils.writeValue(COLOR_FILE_SATURATION, newValue.toString());
                return true;
            case KEY_KCAL_CONTRAST:
                mPrefs.edit().putInt(KEY_KCAL_CONTRAST, (int) newValue).apply();
                FileUtils.writeValue(COLOR_FILE_CONTRAST, newValue.toString());
                return true;
            default:
                throw new RuntimeException("WTF: Unimplemented preference: " + preference.getKey());
        }
    }

    private boolean updateRGB(String key, Integer newVal) {
        // save new color value
        if (!mPrefs.edit().putInt(key, newVal).commit()) return false;

        // apply new RGB color to kernel
        int r = mPrefs.getInt(KEY_KCAL_RED, 256);
        int g = mPrefs.getInt(KEY_KCAL_GREEN, 256);
        int b = mPrefs.getInt(KEY_KCAL_BLUE, 256);

        mKcalColorTemp.setValue(UtilsKCAL.KfromRGB(r, g, b));

        mRed = String.valueOf(r);
        mGreen = String.valueOf(g);
        mBlue = String.valueOf(b);
        String strVal = (mRed + " " + mGreen + " " + mBlue);
        FileUtils.writeValue(COLOR_FILE, strVal);
        return true;
    }
}
