package com.example.childtrackersemi;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence: geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
        }
        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "EXITING IN THE GEOFENCE TRANSITION", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("EXITING IN THE GEOFENCE TRANSITION", "", ParentMonitoringMapActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "DWELLING IN THE GEOFENCE TRANSITION", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("DWELLING IN THE GEOFENCE TRANSITION", "", ParentMonitoringMapActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "INSIDE IN THE GEOFENCE TRANSITION", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("INSIDE IN THE GEOFENCE TRANSITION", "", ParentMonitoringMapActivity.class);
                break;
        }

    }
}
