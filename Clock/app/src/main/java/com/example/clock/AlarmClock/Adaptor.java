package com.example.clock.AlarmClock;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;

import java.util.List;

public class Adaptor extends RecyclerView.Adapter<Adaptor.ViewHolder> {

    List<Model> alarmList;
    Alarm activity;
    DataBase dataBase;
    private LayoutInflater inflater;

    public Adaptor(List<Model> alarmList, Alarm activity,DataBase dataBase)
    {
        this.alarmList=alarmList;
        this.activity=activity;
        this.dataBase=dataBase;
    }
    public ViewHolder onCreateViewHolder(ViewGroup  parent,int viewType)
    {
        View itemView=LayoutInflater.from(parent.getContext()).
                inflate(R.layout.alarm_layout,parent,false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model item=alarmList.get(position);
        holder.hour.setText(item.getHour());
        holder.minutes.setText(item.getMinutes());
        holder.amPm.setText(item.getAmpm());
        holder.alarm.setChecked(toBoolean(item.getSatus()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Model currentItem = alarmList.get(currentPosition);
                    // Remove the alarm from the database
                    dataBase.deleteAlarm(currentItem.getId());
                    // Remove the alarm from the list and notify the adapter
                    alarmList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Model currentItem = alarmList.get(currentPosition);
                    // Prepare the AddAlarm dialog with existing alarm data
                    AddAlarm editAlarmDialog = AddAlarm.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", currentItem.getId());
                    bundle.putString("hour", currentItem.getHour());
                    bundle.putString("minutes", currentItem.getMinutes());
                    bundle.putString("ampm", currentItem.getAmpm());
                    bundle.putString("ringtone", currentItem.getRingtone());
                    editAlarmDialog.setArguments(bundle);
                    //editAlarmDialog.show(fragmentManager, AddAlarm.TAG);

                    // Delete the old alarm before updating
                    dataBase.deleteAlarm(currentItem.getId());
                    alarmList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
            }
        });



    }

    public Alarm getContext()
    {
        return activity;
    }

    private boolean toBoolean(int n)
    {
        return n!=0;
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void setAlarm(List<Model> alarmList)
    {
        this.alarmList=alarmList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView hour;
        TextView minutes;
        TextView amPm;
        Switch alarm;
        ViewHolder(View view)
        {
            super(view);
            alarm=view.findViewById(R.id.switchAlarm);
            hour=view.findViewById(R.id.hour);
            minutes=view.findViewById(R.id.minutes);
            amPm=view.findViewById(R.id.ampm);
        }

    }


}
