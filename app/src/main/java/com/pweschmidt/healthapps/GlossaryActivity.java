package com.pweschmidt.healthapps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
public class GlossaryActivity extends Activity {
//	private static final String TAG = "GlossaryActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gloss_layout);
        ImageButton back = (ImageButton)findViewById(R.id.BackFromGlossaryButton);
        back.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				finish();
			}
		});
    }
}
