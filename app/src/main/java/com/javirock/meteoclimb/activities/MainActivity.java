package com.javirock.meteoclimb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.javirock.meteoclimb.R;
import com.javirock.meteoclimb.models.ApiNetwork;

public class MainActivity extends AppCompatActivity {
    Button mBtnIsAlive = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBtnIsAlive = findViewById(R.id.btn_isalive);
        mBtnIsAlive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ApiNetwork.isAlive();
            }
        });
    }
}
