package com.brianvega.tourmalineserviceexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    Button startServiceBackground;
    private ActivityListener activityListener;
    private LocationListener locationListener;
    private TelematicsEventListener telematicsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService = findViewById(R.id.btn_start_service);
        startServiceBackground = findViewById(R.id.btn_start_service_background);
        loadReferences();
    }

    private void loadReferences() {
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TLKitManager.initTourmalineService(
                        activityListener,
                        telematicsListener,
                        locationListener,
                        Constants.EMAIL_USER
                        );
            }
        });
        startServiceBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplication().startService(new Intent(getApplicationContext(), TLKitService.class));
            }
        });
    }

}
