package com.brianvega.tourmalineserviceexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brianvega.tourmalineserviceexample.tourmalineService.TLKitManager;
import com.brianvega.tourmalineserviceexample.tourmalineService.TLKitService;
import com.tourmaline.context.ActivityListener;
import com.tourmaline.context.LocationListener;
import com.tourmaline.context.TelematicsEventListener;

public class MainActivity extends AppCompatActivity {

    Button startService;
    Button startServiceInBackground;
    private ActivityListener activityListener;
    private LocationListener locationListener;
    static public final int REQUEST_LOCATION = 1;
    private TelematicsEventListener telematicsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService = findViewById(R.id.btn_start_service);
        startServiceInBackground = findViewById(R.id.btn_start_service_background);
        loadReferences();
    }

    private void loadReferences() {
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(false);
            }
        });
        startServiceInBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(true);
            }
        });
    }

    private void checkPermission(boolean initService) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
           if (initService) {
               initService();
           } else {
               initTourmalineService();
           }
        }
    }

    private void initTourmalineService() {
        TLKitManager.initTourmalineService(
                activityListener,
                telematicsListener,
                locationListener,
                Constants.EMAIL_USER
        );
    }

    private void initService() {
        getApplication().startService(new Intent(getApplicationContext(), TLKitService.class));
    }
}
