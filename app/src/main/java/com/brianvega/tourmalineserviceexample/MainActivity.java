package com.brianvega.tourmalineserviceexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brianvega.tourmalineserviceexample.adapters.LogsRecyclerAdapter;
import com.brianvega.tourmalineserviceexample.models.Message;
import com.brianvega.tourmalineserviceexample.tourmalineService.TLKitManager;
import com.brianvega.tourmalineserviceexample.tourmalineService.TLKitService;
import com.brianvega.tourmalineserviceexample.utils.SharedPreferencesUtil;
import com.tourmaline.context.ActivityListener;
import com.tourmaline.context.LocationListener;
import com.tourmaline.context.TelematicsEventListener;

public class MainActivity extends AppCompatActivity {

    TextView currentUser;
    EditText editUserName;
    EditText editPassword;
    Button btnChangeUser;
    Button startService;
    Button startServiceInBackground;
    Button showLogs;
    Button hideLogs;
    Button btnUpdateLogs;
    RecyclerView rcvLog;
    LogsRecyclerAdapter logsAdapter;
    Guideline guideLineLogs;
    private ActivityListener activityListener;
    private LocationListener locationListener;
    static public final int REQUEST_LOCATION = 1;
    private TelematicsEventListener telematicsListener;
    String userName = null;
    String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFindViewId();
        loadReferences();
    }

    private void loadReferences() {
        verifyUser();
        loadOnClick();
        loadRecyclerView();
    }

    private void loadRecyclerView() {
        logsAdapter = new LogsRecyclerAdapter(MainApplication.messages);
        rcvLog.setAdapter(logsAdapter);
        rcvLog.setLayoutManager(new LinearLayoutManager(this));
        rcvLog.getAdapter().notifyDataSetChanged();
    }

    private void verifyUser() {
        userName = SharedPreferencesUtil.getEmail(this);
        password = SharedPreferencesUtil.getPassword(this);
        if (userName != null && password != null) {
            currentUser.setText(userName);
            enableItems();
        } else {
            disableItems();
        }
    }

    private void disableItems() {
        startService.setEnabled(false);
        startServiceInBackground.setEnabled(false);
    }

    private void enableItems() {
        startService.setEnabled(true);
        startServiceInBackground.setEnabled(true);
    }

    private void loadFindViewId() {
        startService = findViewById(R.id.btn_start_service);
        startServiceInBackground = findViewById(R.id.btn_start_service_background);
        editPassword = findViewById(R.id.edit_password);
        editUserName = findViewById(R.id.edit_user_name);
        currentUser = findViewById(R.id.txt_current_user);
        btnChangeUser = findViewById(R.id.btn_change_password);
        guideLineLogs = findViewById(R.id.guideline_logs);
        showLogs = findViewById(R.id.btn_show_logs);
        hideLogs = findViewById(R.id.btn_hide_logs);
        rcvLog = findViewById(R.id.rcv_messages);
        btnUpdateLogs = findViewById(R.id.btn_update_logs);
    }

    private void loadOnClick() {
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
        btnChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyFieldUser();        
            }
        });
        showLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideLineLogs.setGuidelinePercent(0.4f);
            }
        });
        hideLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideLineLogs.setGuidelinePercent(1f);
            }
        });
        btnUpdateLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logsAdapter.setMessage(MainApplication.messages);
                rcvLog.setAdapter(logsAdapter);
                rcvLog.getAdapter().notifyDataSetChanged();
            }
        });
    }
    
    private void verifyFieldUser() {
        if (!TextUtils.isEmpty(editUserName.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString())) {
            SharedPreferencesUtil.saveEmail(this, editUserName.getText().toString());
            SharedPreferencesUtil.savePassword(this, editPassword.getText().toString());
            verifyUser();
        } else {
            Toast.makeText(this, "Enter a valid user", Toast.LENGTH_SHORT).show();
        }
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
