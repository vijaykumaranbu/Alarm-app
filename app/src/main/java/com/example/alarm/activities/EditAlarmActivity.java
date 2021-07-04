package com.example.alarm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.R;
import com.example.alarm.dataBase.ReminderDataBase;
import com.example.alarm.dataBase.SharedPreference;
import com.example.alarm.dialogs.AlertBottomSheetDialog;
import com.example.alarm.dialogs.LabelDialog;
import com.example.alarm.dialogs.SnoozeBottomSheetDialog;
import com.example.alarm.modals.Reminder;
import com.example.alarm.receivers.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class EditAlarmActivity extends AppCompatActivity implements
        View.OnClickListener,
        AlertBottomSheetDialog.AlertBottomSheetListener,
        SnoozeBottomSheetDialog.SnoozeBottomSheetListener,
        LabelDialog.LabelDialogListener {

    public static final String REMINDER_ID = "Reminder_ID";
    private TimePicker timePicker;
    private TextView cancel, done;
    private LinearLayout repeatLayout, alertModeLayout, snoozeLayout, labelLayout;
    private RelativeLayout ringtoneLayout;
    private TextView repeatView, alertModeView, ringtoneNameView, snoozeView, labelView;
    private Button sun, mon, tue, wed, thu, fri, sat;
    private AlertBottomSheetDialog alertBottomSheetDialog;
    private SnoozeBottomSheetDialog snoozeBottomSheetDialog;
    private long mRepeatTime;
    private int ID, hour, mHour, mMinute;
    private String hr, min, mTime, mAm_Pm, mDays;
    private Calendar mCalendar;
    private AlarmReceiver alarmReceiver;
    private Reminder mReceivedReminder;
    private ArrayList<String> repeatList = new ArrayList<>();
    private TreeMap<Integer, String> dayMap = new TreeMap<>();
    public static final String EDIT_ALERT_BOTTOM_SHEET_TAG = "edit_alert_bottom_sheet";
    public static final String EDIT_SNOOZE_BOTTOM_SHEET_TAG = "edit_snooze_bottom_sheet";
    public static final String EDIT_LABEL_DIALOG_TAG = "edit_label_dialog";
    private ReminderDataBase db;
    private AddAlarmActivity addAlarmActivity = new AddAlarmActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        timePicker = findViewById(R.id.edit_alarm_time_picker);
        cancel = findViewById(R.id.edit_alarm_Cancel);
        done = findViewById(R.id.edit_alarm_Done);
        repeatLayout = findViewById(R.id.edit_repeat_linear_layout);
        alertModeLayout = findViewById(R.id.edit_alert_mode_linear_layout);
        snoozeLayout = findViewById(R.id.edit_snooze_linear_layout);
        labelLayout = findViewById(R.id.edit_label_linear_layout);
        ringtoneLayout = findViewById(R.id.edit_ringtone_relative_layout);

        repeatView = findViewById(R.id.edit_repeat_txt_view);
        alertModeView = findViewById(R.id.edit_alert_mode_txt_view);
        ringtoneNameView = findViewById(R.id.edit_ringtone_name_txt_view);
        snoozeView = findViewById(R.id.edit_snooze_txt_view);
        labelView = findViewById(R.id.edit_label_txt_view);

        sun = findViewById(R.id.edit_sun_btn);
        mon = findViewById(R.id.edit_mon_txt_view);
        tue = findViewById(R.id.edit_tue_txt_view);
        wed = findViewById(R.id.edit_wed_txt_view);
        thu = findViewById(R.id.edit_thu_txt_view);
        fri = findViewById(R.id.edit_fri_txt_view);
        sat = findViewById(R.id.edit_sat_txt_view);

        sun.setOnClickListener(this);
        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);
        sat.setOnClickListener(this);

        mCalendar = Calendar.getInstance();
        alarmReceiver = new AlarmReceiver();
        ID = Integer.parseInt(getIntent().getStringExtra(REMINDER_ID));
        db = new ReminderDataBase(getApplicationContext());
        mReceivedReminder = db.getReminder(ID);
        if (mReceivedReminder != null) {

            // if days map is empty it will be set none
            if (dayMap.isEmpty()) {
                repeatView.setText("None");
            }

            // set selectable the days
            sun.setSelected(Boolean.parseBoolean(mReceivedReminder.getSun()));
            if (sun.isSelected())
                repeatView.setText(addDaysInRepeatView(1, "Sun"));

            mon.setSelected(Boolean.parseBoolean(mReceivedReminder.getMon()));
            if (mon.isSelected())
                repeatView.setText(addDaysInRepeatView(2, "Mon"));

            tue.setSelected(Boolean.parseBoolean(mReceivedReminder.getTue()));
            if (tue.isSelected())
                repeatView.setText(addDaysInRepeatView(3, "Tue"));

            wed.setSelected(Boolean.parseBoolean(mReceivedReminder.getWed()));
            if (wed.isSelected())
                repeatView.setText(addDaysInRepeatView(4, "Wed"));

            thu.setSelected(Boolean.parseBoolean(mReceivedReminder.getThu()));
            if (thu.isSelected())
                repeatView.setText(addDaysInRepeatView(5, "Thu"));

            fri.setSelected(Boolean.parseBoolean(mReceivedReminder.getFri()));
            if (fri.isSelected())
                repeatView.setText(addDaysInRepeatView(6, "Fri"));

            sat.setSelected(Boolean.parseBoolean(mReceivedReminder.getSat()));
            if (sat.isSelected())
                repeatView.setText(addDaysInRepeatView(7, "Sat"));

            // set text in AlertMode textView
            alertModeView.setText(mReceivedReminder.getAlertMode());
            SharedPreference.setPreferenceData(AlertBottomSheetDialog.ALERT_MODE_TAG, mReceivedReminder.getAlertMode());

            // set text in Ringtone textView
            ringtoneNameView.setText(SharedPreference.getAllAudioFromDevice(getApplicationContext()).get(mReceivedReminder.getRingtone()).getAudioName());
            SharedPreference.setPreferenceData(AlarmRingtoneActivity.ALARM_RINGTONE_POSITION_TAG,
                    mReceivedReminder.getRingtone());

            // set text in Snooze textView
            snoozeView.setText(mReceivedReminder.getSnooze());
            SharedPreference.setPreferenceData(SnoozeBottomSheetDialog.SNOOZE_MODE_TAG, mReceivedReminder.getSnooze());

            //  set text in Label textView
            labelView.setText(mReceivedReminder.getLabel());
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                mHour = hourOfDay;
                hour = hourFormat(hourOfDay);
                mMinute = minute;

                hr = String.valueOf(hour);

                if (minute < 10)
                    min = "0" + mMinute;
                else
                    min = String.valueOf(mMinute);

                mTime = hr + ":" + min;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ifSameTimeIsThereInReminders(mTime + " " + mAm_Pm)) {
                    Toast.makeText(getApplicationContext(), "This time already set", Toast.LENGTH_LONG).show();
                } else {
                    editReminder();
                }
            }
        });

        alertModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertBottomSheetDialog = new AlertBottomSheetDialog();
                alertBottomSheetDialog.show(getSupportFragmentManager(), EDIT_ALERT_BOTTOM_SHEET_TAG);
            }
        });

        ringtoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditAlarmActivity.this, AlarmRingtoneActivity.class);
                startActivity(intent);
            }
        });

        snoozeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                snoozeBottomSheetDialog = new SnoozeBottomSheetDialog();
                snoozeBottomSheetDialog.show(getSupportFragmentManager(), EDIT_SNOOZE_BOTTOM_SHEET_TAG);
            }
        });

        labelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LabelDialog labelDialog = new LabelDialog(labelView.getText().toString());
                labelDialog.show(getSupportFragmentManager(), EDIT_LABEL_DIALOG_TAG);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = SharedPreference.getAllAudioFromDevice(getApplicationContext()).get(SharedPreference.getPreferenceDataInt("audio_position")).getAudioName();
        ringtoneNameView.setText(name);
    }


    @Override
    public void onClick(View view) {
        Button dayBtn = (Button) view;
        String day = dayBtn.getText().toString();
        if (dayBtn.isSelected()) {
            dayBtn.setSelected(false);
            if (day.equals("Sun"))
                repeatView.setText(removeDaysInRepeatView(1));
            else if (day.equals("Mon"))
                repeatView.setText(removeDaysInRepeatView(2));
            else if (day.equals("Tue"))
                repeatView.setText(removeDaysInRepeatView(3));
            else if (day.equals("Wed"))
                repeatView.setText(removeDaysInRepeatView(4));
            else if (day.equals("Thu"))
                repeatView.setText(removeDaysInRepeatView(5));
            else if (day.equals("Fri"))
                repeatView.setText(removeDaysInRepeatView(6));
            else if (day.equals("Sat"))
                repeatView.setText(removeDaysInRepeatView(7));
        } else {
            dayBtn.setSelected(true);
            if (day.equals("Sun"))
                repeatView.setText(addDaysInRepeatView(1, day));
            else if (day.equals("Mon"))
                repeatView.setText(addDaysInRepeatView(2, day));
            else if (day.equals("Tue"))
                repeatView.setText(addDaysInRepeatView(3, day));
            else if (day.equals("Wed"))
                repeatView.setText(addDaysInRepeatView(4, day));
            else if (day.equals("Thu"))
                repeatView.setText(addDaysInRepeatView(5, day));
            else if (day.equals("Fri"))
                repeatView.setText(addDaysInRepeatView(6, day));
            else if (day.equals("Sat"))
                repeatView.setText(addDaysInRepeatView(7, day));
        }
    }

    private String removeDaysInRepeatView(Integer key) {

        dayMap.remove(key);

        Collection<String> days = dayMap.values();

        Iterator<String> iterator = days.iterator();
        mDays = "";
        if (iterator.hasNext())
            mDays = iterator.next();
        while (iterator.hasNext()) {
            mDays = mDays + ", " + iterator.next();
        }

        if (dayMap.isEmpty()) {
            mDays = "None";
        }
        return mDays;
    }

    private String addDaysInRepeatView(Integer key, String day) {

        dayMap.put(key, day);
        Collection<String> days = dayMap.values();
        Iterator<String> iterator = days.iterator();
        mDays = "";
        if (iterator.hasNext())
            mDays = iterator.next();
        while (iterator.hasNext()) {
            mDays = mDays + ", " + iterator.next();
        }
        return mDays;
    }

    @Override
    public void OnModeClicked(String mode) {
        alertModeView.setText(mode);
    }

    @Override
    public void OnTimeClicked(String time) {
        snoozeView.setText(time);
    }

    public void editReminder() {

        String mSun = String.valueOf(sun.isSelected());
        String mMon = String.valueOf(mon.isSelected());
        String mTue = String.valueOf(tue.isSelected());
        String mWed = String.valueOf(wed.isSelected());
        String mThu = String.valueOf(thu.isSelected());
        String mFri = String.valueOf(fri.isSelected());
        String mSat = String.valueOf(sat.isSelected());
        String mAlertMode = alertModeView.getText().toString();
        int mRingtone = SharedPreference.getPreferenceDataInt("audio_position");
        String mSnooze = snoozeView.getText().toString();
        String mLabel;
        if (labelView.getText().toString().equalsIgnoreCase("none"))
            mLabel = "";
        else
            mLabel = labelView.getText().toString();


        mReceivedReminder.setHour(mHour);
        mReceivedReminder.setMinute(mMinute);
        mReceivedReminder.setAm_pm(mAm_Pm);
        mReceivedReminder.setSun(mSun);
        mReceivedReminder.setMon(mMon);
        mReceivedReminder.setTue(mTue);
        mReceivedReminder.setWed(mWed);
        mReceivedReminder.setThu(mThu);
        mReceivedReminder.setFri(mFri);
        mReceivedReminder.setSat(mSat);
        mReceivedReminder.setDays(mDays);
        mReceivedReminder.setAlertMode(mAlertMode);
        mReceivedReminder.setRingtone(mRingtone);
        mReceivedReminder.setSnooze(mSnooze);
        mReceivedReminder.setLabel(mLabel);
        mReceivedReminder.setActive("true");

        db.updateReminder(mReceivedReminder);

        alarmReceiver.cancelAlarm(getApplicationContext(), ID);

        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        if (mCalendar.before(Calendar.getInstance())) {
            mCalendar.add(Calendar.DATE, 1);
        }

        if (mSnooze.equals("5 Minutes"))
            mRepeatTime = 60000 * 5;

        else if (mSnooze.equals("10 Minutes"))
            mRepeatTime = 60000 * 10;

        else if (mSnooze.equals("15 Minutes"))
            mRepeatTime = 60000 * 15;

        else if (mSnooze.equals("30 Minutes"))
            mRepeatTime = 60000 * 30;

        else if (mSnooze.equals("1 Hour"))
            mRepeatTime = 60000 * 30 * 2;

        if (mSnooze.equals("None"))
            alarmReceiver.setAlarm(getApplicationContext(), mCalendar, ID);
        else
            alarmReceiver.setRepeatAlarm(getApplicationContext(), mCalendar, ID, mRepeatTime);

        // set Day Repeat
        if (sun.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), (int) ID, 1, mHour, mMinute);
        else if (mon.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), (int) ID, 2, mHour, mMinute);
        else if (tue.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), (int) ID, 3, mHour, mMinute);
        else if (wed.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), (int) ID, 4, mHour, mMinute);
        else if (thu.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), (int) ID, 5, mHour, mMinute);
        else if (fri.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), (int) ID, 6, mHour, mMinute);
        else if (sat.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), (int) ID, 7, mHour, mMinute);

        onBackPressed();
    }


    @Override
    public void labelListener(String text) {
        labelView.setText(text);
        if (text != null)
            SharedPreference.setPreferenceData(AddAlarmActivity.LABEL_TEXT, text);
    }

    private int hourFormat(int hour) {
        if (hour > 11) {
            mAm_Pm = "PM";
            if (hour > 12)
                hour -= 12;
        } else {
            mAm_Pm = "AM";
            if (hour == 0)
                hour = 12;
        }

        return hour;
    }

    public boolean ifSameTimeIsThereInReminders(String timeStr) {

        ReminderDataBase db = new ReminderDataBase(getApplicationContext());
        List<Reminder> reminders = db.getAllReminders();
        ArrayList<String> rTimes = new ArrayList<>();
        int hour = 0;
        for (Reminder reminder : reminders) {
            hour = AddAlarmActivity.reminderHourFormat(reminder.getHour());
            rTimes.add("" + hour + ":" + reminder.getMinute() + " " + reminder.getAm_pm());
        }
        boolean mTrueOrFalse = false;
        for (String str : rTimes) {
            if (str.equals(timeStr)) {
                mTrueOrFalse = true;
                break;
            } else {
                mTrueOrFalse = false;
            }
        }
        return mTrueOrFalse;
    }
}