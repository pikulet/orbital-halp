package com.kayheenjoyce.halp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_registration);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.reverse_enter, R.anim.reverse_exit);
    }
}
