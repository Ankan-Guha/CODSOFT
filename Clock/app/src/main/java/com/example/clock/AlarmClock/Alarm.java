package com.example.clock.AlarmClock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.clock.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Alarm extends AppCompatActivity implements DialogCloseListener{


    private RecyclerView taskRecyclerView;
    private Adaptor alarmAdapter;
    private List<Model> alarmList;
    //database
    private DataBase dataBase;
    //add button
    private FloatingActionButton fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        dataBase = new DataBase(this);
        dataBase.openDatabase();

        alarmList=new ArrayList<>();
        alarmAdapter=new Adaptor(alarmList,Alarm.this,dataBase);

        taskRecyclerView=findViewById(R.id.listAlarm);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(alarmAdapter);

        //delete
        //edit

        alarmList.addAll(dataBase.getAllAlarms());
        alarmAdapter.setAlarm(alarmList);

        fb = findViewById(R.id.fabAddAlarm);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlarm.newInstance().show(getSupportFragmentManager(), AddAlarm.TAG);//13
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        alarmList.clear();
        alarmList.addAll(dataBase.getAllAlarms());
        alarmAdapter.setAlarm(alarmList);
        alarmAdapter.notifyDataSetChanged();

    }
}