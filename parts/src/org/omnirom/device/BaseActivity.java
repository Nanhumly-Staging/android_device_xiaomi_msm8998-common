package org.omnirom.device;

import android.app.Activity;
import android.view.MenuItem;

public class BaseActivity extends Activity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
