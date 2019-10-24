package com.brianvega.tourmalineserviceexample.tourmalineService;

import android.content.Context;

public class Monitoring {

    private static final String MONITORING_STATE = "PrefMonitoringState";

    public enum State { STOPPED, AUTOMATIC, MANUAL }

    public static State getState(final Context context) {
        final int state = Preferences.getInstance(context).getInt(MONITORING_STATE, State.STOPPED.ordinal());
        return (State.values())[state];
    }

    public static void setState(final Context context, final State state) {
        Preferences.getInstance(context).putInt(MONITORING_STATE, state.ordinal());
    }

}