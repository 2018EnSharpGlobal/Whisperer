package com.ensharp.kimyejin.voicerecognitiontest.MapManager;

import android.os.Bundle;
import android.widget.TextView;

import com.ensharp.kimyejin.voicerecognitiontest.LocationVO;
import com.ensharp.kimyejin.voicerecognitiontest.MainActivity;
import com.ensharp.kimyejin.voicerecognitiontest.R;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;

public class IndoorAtlas{

    private MainActivity main;
    private LocationVO location = LocationVO.getInstance();

    private ProcedureManager procedureManager;

    public IndoorAtlas(MainActivity main) {
        this.main = main;
        procedureManager = new ProcedureManager();
    }

    IALocationListener locationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {

            //((TextView)main.findViewById(R.id.indoorText)).setText(String.valueOf(iaLocation.getLatitude() +","+iaLocation.getLongitude()+" , "+iaLocation.getBearing()+","+iaLocation.getFloorLevel()));
            location.setLocation(iaLocation.getLatitude(), iaLocation.getLongitude(), iaLocation.getFloorLevel());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    public IALocationManager getLocationManager() {return IALocationManager.create(main);}
    public IALocationListener getLocationListener() {return locationListener;}
}
