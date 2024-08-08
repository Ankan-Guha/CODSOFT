package com.example.clock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clock.AlarmClock.Alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageView hourHand, minuteHand, secondHand,alarm;
    private Handler handler;
    private Runnable runnable;

    private TextView dateTextView;
    private TextView timeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hourHand = findViewById(R.id.hourHand);
        minuteHand = findViewById(R.id.minuteHand);
        secondHand = findViewById(R.id.secondHand);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);

        updateDateTime();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateClock();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        alarm=findViewById(R.id.alarm);
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Alarm clock", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Alarm.class);
                startActivity(intent);
            }
        });

    }

    private void updateClock() {
        Calendar calendar = Calendar.getInstance();

        float second = calendar.get(Calendar.SECOND);
        float minute = calendar.get(Calendar.MINUTE);
        float hour = calendar.get(Calendar.HOUR);

        float secondRotation = second * 6;
        float minuteRotation = minute * 6 + second * 0.1f;
        float hourRotation = hour * 30 + minute * 0.5f;

        RotateAnimation secondAnimation = new RotateAnimation(secondRotation, secondRotation,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        secondAnimation.setDuration(1000);
        secondAnimation.setFillAfter(true);
        secondHand.startAnimation(secondAnimation);

        RotateAnimation minuteAnimation = new RotateAnimation(minuteRotation, minuteRotation,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        minuteAnimation.setDuration(1000);
        minuteAnimation.setFillAfter(true);
        minuteHand.startAnimation(minuteAnimation);

        RotateAnimation hourAnimation = new RotateAnimation(hourRotation, hourRotation,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hourAnimation.setDuration(1000);
        hourAnimation.setFillAfter(true);
        hourHand.startAnimation(hourAnimation);
    }
    private void updateDateTime() {
        // Get the current date and time
        String currentDate = new SimpleDateFormat("MMMM d, yyyy\nEEEE ", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        // Set the date and time to TextViews
        dateTextView.setText(currentDate);
        timeTextView.setText(currentTime);
    }
}
