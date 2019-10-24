package com.brianvega.tourmalineserviceexample.tourmalineService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.brianvega.tourmalineserviceexample.Constants;
import com.brianvega.tourmalineserviceexample.MainApplication;
import com.brianvega.tourmalineserviceexample.utils.SharedPreferencesUtil;
import com.tourmaline.context.ActivityListener;
import com.tourmaline.context.LocationListener;
import com.tourmaline.context.TelematicsEventListener;

public class TLKitService extends Service {

    private Handler handler = new Handler();
    private ActivityListener activityListener;
    private LocationListener locationListener;
    private TelematicsEventListener telematicsListener;

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            Context context = getApplicationContext();
            Intent myIntent = new Intent(context, TLKitService.class);
            context.startService(myIntent);
            handler.postDelayed(this, 600000);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.runnableCode);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.handler.post(this.runnableCode);
        TLKitManager.startMonitoring(
                Monitoring.State.AUTOMATIC,
                MainApplication.getAppContext(),
                activityListener,
                telematicsListener,
                locationListener,
                Constants.EMAIL_USER
        );
        return START_STICKY;
    }
}
