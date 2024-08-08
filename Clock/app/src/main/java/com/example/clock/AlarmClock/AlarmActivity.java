package com.example.clock.AlarmClock;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.example.clock.R;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private static final String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm2);

        // Start playing the alarm sound
        String ringtoneUri = getIntent().getStringExtra("ringtone");
        if (ringtoneUri != null) {
            mediaPlayer = MediaPlayer.create(this, Uri.parse(ringtoneUri));
            mediaPlayer.setLooping(true); // Optional: loop the alarm sound
            mediaPlayer.start();
        } else {
            Log.e(TAG, "Ringtone URI is null");
        }

        findViewById(R.id.snooze).setOnClickListener(v -> snoozeAlarm());
        findViewById(R.id.stop).setOnClickListener(v -> turnOffAlarm());
    }

    private void snoozeAlarm() {
        // Set snooze alarm for 10 minutes later
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    // For API 31 and above
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            } catch (SecurityException e) {
                Log.e(TAG, "Permission to set exact alarms denied", e);
            }
        }

        finish();
    }

    private Ringtone ringtone;

    private void playAlarm() {
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
        if (ringtone != null) {
            ringtone.setLooping(true); // Loop the ringtone
            ringtone.play();
        }
    }

    private void turnOffAlarm() {
        Log.d(TAG, "Stop button clicked. Attempting to stop the alarm.");
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                Log.d(TAG, "Alarm stopped.");
            } else {
                Log.d(TAG, "MediaPlayer is not playing, no action taken.");
            }
            mediaPlayer.release();
            mediaPlayer = null; // Avoid memory leaks
        } else {
            Log.d(TAG, "MediaPlayer was null, nothing to stop.");
        }
        onDestroy();
        finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null; // Avoid memory leaks
        }
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
            ringtone = null; // Avoid memory leaks
        }
    }

}
