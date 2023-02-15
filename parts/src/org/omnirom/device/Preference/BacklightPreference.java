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
import android.widget.SeekBar;

import org.omnirom.device.utils.FileUtils;

/**
 * Backlight Preference used to adjust led brightness of buttons.
 * <p>
 * Created by 0ranko0P <ranko0p@outlook.com> on 2019.10.30
 */
public final class BacklightPreference extends SeekBarPreferenceCham {

    public static final String KEY_BTN_BRIGHTNESS = "btn_brightness";

    private static final int BACKLIGHT_MIN_BRIGHTNESS = 0;
    private static final int BACKLIGHT_MAX_BRIGHTNESS = 255;
    private static final float PROGRESS_OFFSET = BACKLIGHT_MAX_BRIGHTNESS / 100f;

    private static final String FILE_LED_LEFT = "/sys/class/leds/button-backlight/max_brightness";
    private static final String FILE_LED_RIGHT = "/sys/class/leds/button-backlight1/max_brightness";

    public static KernelFeature<Integer> FEATURE = new KernelFeature<Integer>() {

        @Override
        public boolean isSupported() {
            return FileUtils.isFileWritable(FILE_LED_LEFT) && FileUtils.isFileWritable(FILE_LED_RIGHT);
        }

        /**
         * @return Button brightness value that currently in use
         */
        @Override
        public Integer getCurrentValue() {
            // Read the value that currently in use, not the one from sp.
            // User might modify this value though some kernel manager.
            String currentVal = FileUtils.getFileValue(FILE_LED_LEFT, null);
            return currentVal == null ? BACKLIGHT_MAX_BRIGHTNESS : Integer.valueOf(currentVal);
        }

        @Override
        public boolean applyValue(Integer newValue) {
            String newStrVal = newValue.toString();
            return FileUtils.writeValue(FILE_LED_LEFT, newStrVal) &&
                    FileUtils.writeValue(FILE_LED_RIGHT, newStrVal);
        }

        @Override
        public void applySharedPreferences(Integer newValue, SharedPreferences sp) {
            sp.edit().putInt(KEY_BTN_BRIGHTNESS, newValue).apply();
        }

        @Override
        public boolean restore(SharedPreferences sp) {
            if (!isSupported()) return false;

            int storedValue = sp.getInt(KEY_BTN_BRIGHTNESS, BACKLIGHT_MAX_BRIGHTNESS);
            return applyValue(storedValue);
        }
    };

    public BacklightPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setProgress(FEATURE.getCurrentValue());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        super.onProgressChanged(seekBar, progress, fromTouch);
        FEATURE.applyValue(progress);
    }
}