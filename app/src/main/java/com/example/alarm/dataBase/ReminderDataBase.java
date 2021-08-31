package com.example.alarm.dataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.alarm.modals.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderDataBase extends SQLiteOpenHelper {

    private Context context;

    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "Reminder_DataBase.db";
    private static final String TABLE_NAME = "Reminder";

    private static final String KEY_ID = "ID";
    private static final String KEY_HOUR = "HOUR";
    private static final String KEY_MINUTE = "MINUTE";
    private static final String KEY_AM_PM = "AM_PM";
    private static final String KEY_SUN = "SUN";
    private static final String KEY_MON = "MON";
    private static final String KEY_TUE = "TUE";
    private static final String KEY_WED = "WED";
    private static final String KEY_THU = "THU";
    private static final String KEY_FRI = "FRI";
    private static final String KEY_SAT = "SAT";
    private static final String KEY_DAYS = "DAYS";
    private static final String KEY_ALERT_MODE = "ALERT_MODE";
    private static final String KEY_RINGTONE = "RINGTONE";
    private static final String KEY_SNOOZE = "SNOOZE";
    private static final String KEY_LABEL = "LABEL";
    private static final String KEY_ACTIVE = "ACTIVE";

    public ReminderDataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_HOUR + " INTEGER,"
                + KEY_MINUTE + " INTEGER,"
                + KEY_AM_PM + " TEXT,"
                + KEY_SUN + " TEXT,"
                + KEY_MON + " TEXT,"
                + KEY_TUE + " TEXT,"
                + KEY_WED + " TEXT,"
                + KEY_THU + " TEXT,"
                + KEY_FRI + " TEXT,"
                + KEY_SAT + " TEXT,"
                + KEY_DAYS + " TEXT,"
                + KEY_ALERT_MODE + " TEXT,"
                + KEY_RINGTONE + " INTEGER,"
                + KEY_SNOOZE + " TEXT,"
                + KEY_LABEL + " TEXT,"
                + KEY_ACTIVE + " TEXT"
                + ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion >= newVersion)
            return;

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public int addReminder(Reminder reminder){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_HOUR,reminder.getHour());
        values.put(KEY_MINUTE,reminder.getMinute());
        values.put(KEY_AM_PM,reminder.getAm_pm());
        values.put(KEY_SUN,reminder.getSun());
        values.put(KEY_MON,reminder.getMon());
        values.put(KEY_TUE,reminder.getTue());
        values.put(KEY_WED,reminder.getWed());
        values.put(KEY_THU,reminder.getThu());
        values.put(KEY_FRI,reminder.getFri());
        values.put(KEY_SAT,reminder.getSat());
        values.put(KEY_DAYS,reminder.getDays());
        values.put(KEY_ALERT_MODE,reminder.getAlertMode());
        values.put(KEY_RINGTONE,reminder.getRingtone());
        values.put(KEY_SNOOZE,reminder.getSnooze());
        values.put(KEY_LABEL,reminder.getLabel());
        values.put(KEY_ACTIVE,reminder.getActive());

        long ID = db.insert(TABLE_NAME,null,values);
        db.close();
        return (int)ID;
    }

    public Reminder getReminder(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] {
                        KEY_ID,
                        KEY_HOUR,
                        KEY_MINUTE,
                        KEY_AM_PM,
                        KEY_SUN,
                        KEY_MON,
                        KEY_TUE,
                        KEY_WED,
                        KEY_THU,
                        KEY_FRI,
                        KEY_SAT,
                        KEY_DAYS,
                        KEY_ALERT_MODE,
                        KEY_RINGTONE,
                        KEY_SNOOZE,
                        KEY_LABEL,
                        KEY_ACTIVE
                },KEY_ID + "=?",new String[] {String.valueOf(id)},null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Reminder reminder = new Reminder(Integer.parseInt(cursor.getString(0)),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getString(10),
                cursor.getString(11),
                cursor.getString(12),
                cursor.getInt(13),
                cursor.getString(14),
                cursor.getString(15),
                cursor.getString(16));
        return reminder;
    }

    public List<Reminder> getAllReminders(){
        List<Reminder> reminderList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT*FROM " + TABLE_NAME;

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Reminder reminder = new Reminder();
                reminder.setID(Integer.parseInt(cursor.getString(0)));
                reminder.setHour(Integer.parseInt(cursor.getString(1)));
                reminder.setMinute(Integer.parseInt(cursor.getString(2)));
                reminder.setAm_pm(cursor.getString(3));
                reminder.setSun(cursor.getString(4));
                reminder.setMon(cursor.getString(5));
                reminder.setTue(cursor.getString(6));
                reminder.setWed(cursor.getString(7));
                reminder.setThu(cursor.getString(8));
                reminder.setFri(cursor.getString(9));
                reminder.setSat(cursor.getString(10));
                reminder.setDays(cursor.getString(11));
                reminder.setAlertMode(cursor.getString(12));
                reminder.setRingtone(cursor.getInt(13));
                reminder.setSnooze(cursor.getString(14));
                reminder.setLabel(cursor.getString(15));
                reminder.setActive(cursor.getString(16));

                reminderList.add(reminder);
            }while (cursor.moveToNext());
        }
        return reminderList;
    }

    public int getRemindersCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT*FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();
        return cursor.getCount();
    }

    public void updateReminder(Reminder reminder){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HOUR,reminder.getHour());
        values.put(KEY_MINUTE,reminder.getMinute());
        values.put(KEY_AM_PM,reminder.getAm_pm());
        values.put(KEY_SUN,reminder.getSun());
        values.put(KEY_MON,reminder.getMon());
        values.put(KEY_TUE,reminder.getTue());
        values.put(KEY_WED,reminder.getWed());
        values.put(KEY_THU,reminder.getThu());
        values.put(KEY_FRI,reminder.getFri());
        values.put(KEY_SAT,reminder.getSat());
        values.put(KEY_DAYS,reminder.getDays());
        values.put(KEY_ALERT_MODE,reminder.getAlertMode());
        values.put(KEY_RINGTONE,reminder.getRingtone());
        values.put(KEY_SNOOZE,reminder.getSnooze());
        values.put(KEY_LABEL,reminder.getLabel());
        values.put(KEY_ACTIVE,reminder.getActive());

        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(reminder.getID())});
    }

    public void deleteReminder(int ID){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID + "=?",new String[]{String.valueOf(ID)});
        db.close();
    }
}
