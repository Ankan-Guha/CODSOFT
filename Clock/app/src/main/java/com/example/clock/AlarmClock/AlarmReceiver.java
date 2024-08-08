package com.example.clock.AlarmClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the ringtone URI from the intent
        String ringtoneUriString = intent.getStringExtra("ringtone");
        Uri ringtoneUri = ringtoneUriString != null ? Uri.parse(ringtoneUriString) : RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        // Play the ringtone
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        ringtone.play();

        // Start the AlarmActivity
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.putExtra("ringtone", ringtoneUriString);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}

