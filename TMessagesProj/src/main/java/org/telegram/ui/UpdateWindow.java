package org.telegram.ui;

import android.os.Bundle;
import android.app.Activity;
import org.telegram.messenger.R;

public class UpdateWindow extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_window);
    }

}
