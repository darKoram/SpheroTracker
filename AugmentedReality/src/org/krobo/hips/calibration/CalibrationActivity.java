package org.krobo.hips.calibration;

import org.krobo.hips.augmentedreality.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CalibrationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_calibration, menu);
        return true;
    }
}
