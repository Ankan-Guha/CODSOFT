package com.example.clock.AlarmClock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper  {

    private static final int VERSION = 2;
    private static final String NAME = "AlarmClock";
    private static final String CLOCK_TABLE = "Alarm";
    private static final String ID = "id";
    private static final String HOUR = "hour";
    private static final String MINUTES = "minutes";
    private static final String RINGTONE = "ringtone";
    private static final String STATUS = "status";
    private static final String AMPM= "ampm";
    private static final String CREATE_CLOCK_TABLE = "CREATE TABLE " +
            CLOCK_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HOUR + " INTEGER, " +
            MINUTES + " INTEGER, " +
            RINGTONE + " TEXT, " +
            STATUS + " INTEGER, " +
            AMPM + " TEXT)";
    private SQLiteDatabase db;
    public DataBase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLOCK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DROP OLDER TABLES
        db.execSQL("DROP TABLE IF EXISTS " + CLOCK_TABLE);
        //CREATE TABLE AGAIN
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertAlarm(Model alarm) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HOUR, alarm.getHour());
        contentValues.put(MINUTES, alarm.getMinutes());
        contentValues.put(RINGTONE, alarm.getRingtone());
        contentValues.put(STATUS, 1);
        contentValues.put(AMPM, alarm.getAmpm());
        db.insert(CLOCK_TABLE, null, contentValues);
    }

    public void updateAlarm(Model alarm) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HOUR, alarm.getHour());
        contentValues.put(MINUTES, alarm.getMinutes());
        contentValues.put(RINGTONE, alarm.getRingtone());
        contentValues.put(AMPM, alarm.getAmpm());
        db.update(CLOCK_TABLE, contentValues, ID + " = ?", new String[]{String.valueOf(alarm.getId())});
    }
    public void deleteAlarm(int id) {
        db.delete(CLOCK_TABLE, ID + " = ?", new String[]{String.valueOf(id)});
    }
    public List<Model> getAllAlarms() {
        List<Model> alarms = new ArrayList<>();
        String orderBy = HOUR + " ASC, " + MINUTES + " ASC";
        Cursor cursor = db.query(CLOCK_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Model alarm = new Model();
                alarm.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                alarm.setHour(cursor.getString(cursor.getColumnIndexOrThrow(HOUR)));
                alarm.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(MINUTES)));
                alarm.setRingtone(cursor.getString(cursor.getColumnIndexOrThrow(RINGTONE)));
                alarm.setSatus(cursor.getInt(cursor.getColumnIndexOrThrow(STATUS)));
                alarm.setAmpm(cursor.getString(cursor.getColumnIndexOrThrow(AMPM)));
                alarms.add(alarm);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return alarms;
    }

    public void updateStatus(int id,int status)
    {
        ContentValues cv=new ContentValues();
        cv.put(STATUS,status);
        db.delete(CLOCK_TABLE, ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Model getAlarmById(int id) {
        Model alarm = null;
        Cursor cursor = null;
        try {
            cursor = db.query(CLOCK_TABLE, null, ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                alarm = new Model();
                alarm.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                alarm.setHour(cursor.getString(cursor.getColumnIndexOrThrow(HOUR)));
                alarm.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(MINUTES)));
                alarm.setRingtone(cursor.getString(cursor.getColumnIndexOrThrow(RINGTONE)));
                alarm.setSatus(cursor.getInt(cursor.getColumnIndexOrThrow(STATUS)));
                alarm.setAmpm(cursor.getString(cursor.getColumnIndexOrThrow(AMPM)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return alarm;
    }

}
