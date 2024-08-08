package com.example.clock.AlarmClock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class AddAlarm extends BottomSheetDialogFragment {
    private String ringtoneUriString;
    public static final String TAG = "ActionBottomDialog";
    private TextView cut, save, ringtone, ringtoneName;
    private EditText hour, minutes, ampm;
    private DataBase db;

    private int alarmId = -1;

    public static AddAlarm newInstance() {
        return new AddAlarm();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            alarmId = getArguments().getInt("id", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_alarm, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hour = getView().findViewById(R.id.hour);
        minutes = getView().findViewById(R.id.minutes);
        ampm = getView().findViewById(R.id.ampm);
        cut = getView().findViewById(R.id.close);
        save = getView().findViewById(R.id.saveAlarm);
        ringtone = getView().findViewById(R.id.ringtone);
        ringtoneName = getView().findViewById(R.id.ringtonename);

        db = new DataBase(getActivity());
        db.openDatabase();

        ringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickRingtone();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String h = hour.getText().toString();
                String m = minutes.getText().toString();
                String a = ampm.getText().toString();
                String r = ringtoneUriString;

                if (h.isEmpty() || m.isEmpty() || a.isEmpty()) {
                    // Show error message to the user
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Model model = new Model();
                model.setSatus(1);
                model.setHour(h);
                model.setMinutes(m);
                model.setRingtone(r);
                model.setAmpm(a);

                if (alarmId == -1) {
                    db.insertAlarm(model);
                } else {
                    model.setId(alarmId);
                    db.updateAlarm(model);
                }

                scheduleAlarm(h, m, a);
                dismiss();
            }
        });
    }

    private void scheduleAlarm(String h, String m, String a) {
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(h);
        int minute = Integer.parseInt(m);

        if (a.equalsIgnoreCase("PM") && hour != 12) {
            hour += 12; // Convert PM hour to 24-hour format
        } else if (a.equalsIgnoreCase("AM") && hour == 12) {
            hour = 0; // Midnight case
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("ringtone", ringtoneUriString);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }

    private static final int REQUEST_CODE_PICK_RINGTONE = 1;

    private void pickRingtone() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Ringtone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, REQUEST_CODE_PICK_RINGTONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_RINGTONE && resultCode == Activity.RESULT_OK) {
            Uri ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (ringtoneUri != null) {
                String ringtoneName = RingtoneManager.getRingtone(getActivity(), ringtoneUri).getTitle(getActivity());
                ringtone.setText(ringtoneName);
                ringtoneUriString = ringtoneUri.toString();
            }
        }
    }
}
