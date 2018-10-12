package com.ensharp.kimyejin.voicerecognitiontest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ensharp.kimyejin.voicerecognitiontest.InformationExtractor.Analyzer;
import com.ensharp.kimyejin.voicerecognitiontest.InformationExtractor.SoundManager;
import com.ensharp.kimyejin.voicerecognitiontest.MapManager.IndoorAtlas;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundManager soundManager;
    private Analyzer analyzer;
    private String address = "ec2-54-180-25-173.ap-northeast-2.compute.amazonaws.com";
    private IndoorAtlas indoor;
    private IALocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundManager = new SoundManager(this);
        analyzer = new Analyzer(this, address);
        indoor = new IndoorAtlas(this);

        locationManager = indoor.getLocationManager();

        String[] neededPermissions = {
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ActivityCompat.requestPermissions( this, neededPermissions, Constant.CODE_PERMISSIONS );

        findViewById(R.id.inputButton).setOnClickListener(this);
        findViewById(R.id.outputButton).setOnClickListener(this);
        findViewById(R.id.lineButton).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeLocationUpdates(indoor.getLocationListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(IALocationRequest.create(), indoor.getLocationListener());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.inputButton:
                soundManager.onInputButtonClick();
                analyzer.clearAllInformation();
                break;
            case R.id.outputButton:
                soundManager.onOutputButtonClick();
                break;
            case R.id.lineButton:
                analyzer.analyzeLine();
                ((EditText)findViewById(R.id.detailInformation)).setText(analyzer.getResult());
                break;
        }
    }
}
