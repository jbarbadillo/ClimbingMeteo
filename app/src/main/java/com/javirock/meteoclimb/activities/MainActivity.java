package com.javirock.meteoclimb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.javirock.meteoclimb.R;
import com.javirock.meteoclimb.models.ApiNetwork;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button mEtxauri = null;
    Button mBaltzola = null;
    ApiNetwork api = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = new ApiNetwork();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mEtxauri = findViewById(R.id.btn_etxauri);
        mEtxauri.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    api.getTownPredictionDay("31085");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mBaltzola = findViewById(R.id.btn_baltzola);
        mBaltzola.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    api.getTownPredictionDay("48026");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
