package com.brianvega.tourmalineserviceexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brianvega.tourmalineserviceexample.tourmalineService.TLKitManager;
import com.tourmaline.context.ActivityListener;
import com.tourmaline.context.LocationListener;
import com.tourmaline.context.TelematicsEventListener;

public class MainActivity extends AppCompatActivity {

    Button startService = null;
    private ActivityListener activityListener;
    private LocationListener locationListener;
    private TelematicsEventListener telematicsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService.findViewById(R.id.btn_start_service);
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
    }

}
