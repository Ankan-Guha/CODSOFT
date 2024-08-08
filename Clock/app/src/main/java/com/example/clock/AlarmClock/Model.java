package com.example.clock.AlarmClock;

public class Model {
    private int id, satus;
    private String hour, minutes, ringtone, ampm;

    // Getters and setters
    Model()
    {

    }
    Model(String hour,String  minutes,String ampm,String ringtoneUri, int status)
    {
        this.hour=hour;
        this.minutes=minutes;
        this.ampm=ampm;
        ringtone=ringtoneUri;
        satus=status;
    }
    public int getSatus() {
        return satus;
    }

    public void setSatus(int satus) {
        this.satus = satus;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }
}
