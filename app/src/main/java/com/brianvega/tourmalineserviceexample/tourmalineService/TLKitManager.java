package com.brianvega.tourmalineserviceexample.tourmalineService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.brianvega.tourmalineserviceexample.Constants;
import com.brianvega.tourmalineserviceexample.MainApplication;
import com.brianvega.tourmalineserviceexample.R;
import com.tourmaline.context.ActivityEvent;
import com.tourmaline.context.ActivityListener;
import com.tourmaline.context.ActivityManager;
import com.tourmaline.context.CompletionListener;
import com.tourmaline.context.Engine;
import com.tourmaline.context.Location;
import com.tourmaline.context.LocationListener;
import com.tourmaline.context.LocationManager;
import com.tourmaline.context.NotificationInfo;
import com.tourmaline.context.QueryHandler;
import com.tourmaline.context.TelematicsEvent;
import com.tourmaline.context.TelematicsEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TLKitManager {

    public static final String ApiKey = Constants.API_KEY;
    public static final String TAG = "TLKitManager";

    static void startMonitoring(
            final Monitoring.State monitoring,
            final Context context,
            final ActivityListener activityListener,
            final TelematicsEventListener telematicsListener,
            final LocationListener locationListener,
            String email) {
        if (!Engine.IsInitialized()) {
            initEngine((monitoring==Monitoring.State.MANUAL), new CompletionListener() {
                @Override
                public void OnSuccess() {
                    Monitoring.setState(context, monitoring);
                }

                @Override
                public void OnFail(int i, String s) {
                    Toast.makeText(context, "Error starting the Engine: " + i + " -> " + s, Toast.LENGTH_LONG).show();
                    stopMonitoring(context, activityListener, telematicsListener, locationListener);
                }
            }, context, email);
        } if (!Engine.IsMonitoringDrives()){
            TLKitManager.registerActivityListener(activityListener);
            TLKitManager.registerLocationListener(locationListener);
            TLKitManager.registerTelematicsListener(telematicsListener);
        } else {
            Log.d(TAG,"tourmaline init");
        }
    }

    static void stopMonitoring(final Context context, ActivityListener activityListener,
                               TelematicsEventListener telematicsListener,
                               LocationListener locationListener) {
       destroyEngine(new CompletionListener() {
            @Override
            public void OnSuccess() {
                Monitoring.setState(context, Monitoring.State.STOPPED);
            }
            @Override
            public void OnFail(int i, String s) {

            }
        }, activityListener, telematicsListener, locationListener, context);
    }

    static void queryLocation() {
        LocationManager.QueryLocations(0L, Long.MAX_VALUE, 20, new QueryHandler<ArrayList<Location>>() {
            @Override
            public void Result(ArrayList<Location> locations) {
                Log.d( TAG, "Recorded locations" );
                for( Location location: locations ) {
                    Log.d(TAG, location.toString() );
                }
            }

            @Override
            public void OnFail(int i, String s) {
                Log.e(TAG, "Query failed with err: " + i );
            }
        });
    }

    static void registerLocationListener(LocationListener locationListener) {
        locationListener = new LocationListener() {
            @Override
            public void OnLocationUpdated(Location location) {
                Log.d(TAG, "OnLocationUpdatedLocation OK");
            }

            @Override
            public void RegisterSucceeded() {
                Log.d(TAG, "RegisterSucceededLocation OK");
                //queryLocation();
            }

            @Override
            public void RegisterFailed(int i) {
                Log.d(TAG, "RegisterFailedLocation OK");
            }
        };
        LocationManager.RegisterLocationListener(locationListener);
    }

    static void registerTelematicsListener(TelematicsEventListener telematicsListener) {
        telematicsListener = new TelematicsEventListener() {
            @Override
            public void OnEvent(TelematicsEvent e) {
                Log.d( TAG, "Got telematics event: " + e.getTripId() + ", " + e.getTime() + ", " + e.getDuration() );
            }

            @Override
            public void RegisterSucceeded() {
                Log.d(TAG, "startTelematicsListener OK");
            }

            @Override
            public void RegisterFailed(int i) {
                Log.d(TAG, "startTelematicsListener KO: " + i);
            }
        };
        ActivityManager.RegisterTelematicsEventListener(telematicsListener);
    }

    static void registerActivityListener(ActivityListener activityListener) {
        activityListener = new ActivityListener() {
            @Override
            public void OnEvent(ActivityEvent activityEvent) {
                Log.d(TAG, "Activity Listener: new event");
            }

            @Override
            public void RegisterSucceeded() {
                Log.d(TAG, "Activity Listener: register success");
            }

            @Override
            public void RegisterFailed(int i) {

                Log.d(TAG, "Activity Listener: register failure");
            }
        };
        ActivityManager.RegisterDriveListener(activityListener);
    }

    static void initEngine(final boolean automaticMonitoring,
                           final CompletionListener completionListener,
                           Context activity, String user) {
        Log.d(TAG, "initEngine");
        final String NOTIF_CHANNEL_ID = "background-run-notif-channel-id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            final NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, activity.getText(R.string.foreground_notification_content_text), NotificationManager.IMPORTANCE_NONE);
            channel.setShowBadge(false);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationInfo note = new NotificationInfo(NOTIF_CHANNEL_ID,
                activity.getString(R.string.app_name),
                activity.getString(R.string.foreground_notification_content_text),
                R.mipmap.ic_launcher);

        try {
            Engine.Init(activity.getApplicationContext(),
                    ApiKey,
                    HashId(user),
                    Engine.MonitoringMode.AUTOMATIC,
                    note,
                    completionListener);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public static void initTourmalineService(
            final ActivityListener activityListener,
            final TelematicsEventListener telematicsListener,
            final LocationListener locationListener,
            final String user) {
        final LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(MainApplication.getAppContext());
        try {
            Log.d(TAG, "initTourmalineService");
            mgr.registerReceiver(new BroadcastReceiver() {

                @Override
                public IBinder peekService(Context myContext, Intent service) {
                    Log.d(TAG, "peekService");
                    return super.peekService(myContext, service);
                }

                @Override
                public void onReceive(Context context, Intent i) {
                    Log.d(TAG, "onReceive");
                    int state = i.getIntExtra("state", Engine.INIT_SUCCESS);
                    Log.d(TAG, state + "");
                    switch (state) {
                        case Engine.INIT_SUCCESS: {
                            Log.d(TAG,"tourmaline success initTourmalineService monitoring");
                            TLKitManager.registerActivityListener(activityListener);
                            TLKitManager.registerLocationListener(locationListener);
                            TLKitManager.registerTelematicsListener(telematicsListener);
                            break;
                        }
                        case Engine.INIT_REQUIRED: {
                            Log.d(TAG,"tourmaline required initTourmalineService monitoring");
                            final CompletionListener listener = new CompletionListener() {
                                @Override
                                public void OnSuccess() {
                                    Log.d(TAG,"Completion Listener Success");
                                }

                                @Override
                                public void OnFail(int i, String s) {
                                    Log.d(TAG, "Completion Listener Fail");
                                }
                            };
                            initEngine(true, listener, MainApplication.getAppContext(), user);
                        }
                        case Engine.INIT_FAILURE: {
                            final String msg = i.getStringExtra("message");
                            final int reason = i.getIntExtra("reason", 0);
                            Log.d(TAG, "tourmaline ENGINE INIT FAILURE" + reason + ": " + msg);
                            break;
                        }
                        case Engine.GPS_ENABLED: {
                            Log.d(TAG, "GPS_ENABLED");
                            break;
                        }
                        case Engine.GPS_DISABLED: {
                            Log.d(TAG, "GPS_DISABLED");
                            break;
                        }
                        case Engine.LOCATION_PERMISSION_GRANTED: {
                            Log.d(TAG, "LOCATION_PERMISSION_GRANTED");
                            break;
                        }
                        case Engine.LOCATION_PERMISSION_DENIED: {
                            Log.d(TAG, "LOCATION_PERMISSION_DENIED");
                            break;
                        }
                        case Engine.POWER_SAVE_MODE_DISABLED: {
                            Log.d(TAG, "POWER_SAVE_MODE_DISABLED");
                            break;
                        }
                        case Engine.POWER_SAVE_MODE_ENABLED: {
                            Log.d(TAG, "POWER_SAVE_MODE_ENABLED");
                            break;
                        }
                    }
                }
            }, new IntentFilter(Engine.ACTION_LIFECYCLE));
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    static void destroyEngine(final CompletionListener completionListener,
                              ActivityListener activityListener,
                              TelematicsEventListener telematicsListener,
                              LocationListener locationListener,
                              Context context) {
        if (activityListener != null) {
            ActivityManager.UnregisterDriveListener(activityListener);
            activityListener = null;
        }
        if (telematicsListener != null) {
            ActivityManager.UnregisterTelematicsEventListener(telematicsListener);
            telematicsListener = null;
        }
        if (locationListener != null) {
            LocationManager.UnregisterLocationListener(locationListener);
            locationListener = null;
        }
        Engine.Destroy(context, completionListener);
    }

    static String HashId(String str) {
        String result = "";
        try {
            final MessageDigest digester = MessageDigest.getInstance("SHA-256");
            digester.reset();
            byte[] dig = digester.digest(str.getBytes());
            result = String.format("%0" + (dig.length * 2) + "X", new BigInteger(1, dig)).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No SHA 256 wtf");
        }
        return result;
    }

}
