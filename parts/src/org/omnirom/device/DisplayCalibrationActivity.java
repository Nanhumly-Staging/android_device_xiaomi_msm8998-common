package org.omnirom.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;

public final class DisplayCalibrationActivity extends BaseActivity {
    private DisplayCalibration calibrationFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_calibration_activity);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        calibrationFrag = (DisplayCalibration) getFragmentManager().findFragmentById(R.id.content);
        if (calibrationFrag == null) {
            throw new RuntimeException("WTF: calibration fragment is null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.kcal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_reset) {
            calibrationFrag.reset();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DisplayCalibrationActivity.class);
        context.startActivity(intent);
    }
}
