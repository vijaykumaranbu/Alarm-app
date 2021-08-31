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

public class AddAlarmActivity extends AppCompatActivity implements
        AlertBottomSheetDialog.AlertBottomSheetListener,
        SnoozeBottomSheetDialog.SnoozeBottomSheetListener,
        LabelDialog.LabelDialogListener,
        View.OnClickListener {

    private TextView repeatView, alertModeView, ringtoneNameView, snoozeView, labelView;
    private Button sun, mon, tue, wed, thu, fri, sat;
    private AlertBottomSheetDialog alertBottomSheetDialog;
    private SnoozeBottomSheetDialog snoozeBottomSheetDialog;
    public static final String SNOOZE_BOTTOM_SHEET_TAG = "snooze_bottom_sheet";
    public static final String ALERT_BOTTOM_SHEET_TAG = "alert_bottom_sheet";
    public static final String LABEL_TEXT = "label_text";
    private final TreeMap<Integer, String> dayMap = new TreeMap<>();
    private int hour;
    private int mHour;
    private int mMinute;
    private String mTime, min, hr, mAm_Pm, mDays = "";
    private final AlarmReceiver alarmReceiver = new AlarmReceiver();
    private long mRepeatTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        TextView cancel = findViewById(R.id.alarmCancel);
        TextView done = findViewById(R.id.alarmDone);
        TimePicker timePicker = findViewById(R.id.alarm_time_picker_view);
        LinearLayout repeatLayout = findViewById(R.id.repeat_linear_layout);
        LinearLayout alertModeLayout = findViewById(R.id.alert_mode_linear_layout);
        RelativeLayout ringtoneLayout = findViewById(R.id.ringtone_relative_layout);
        LinearLayout snoozeLayout = findViewById(R.id.snooze_linear_layout);
        LinearLayout labelLayout = findViewById(R.id.label_linear_layout);
        repeatView = findViewById(R.id.repeat_txt_view);
        alertModeView = findViewById(R.id.alert_mode_txt_view);
        ringtoneNameView = findViewById(R.id.ringtone_name_txt_view);
        snoozeView = findViewById(R.id.snooze_txt_view);
        labelView = findViewById(R.id.label_txt_view);
        timePicker = findViewById(R.id.alarm_time_picker_view);
        sun = findViewById(R.id.sun_txt_view);
        mon = findViewById(R.id.mon_txt_view);
        tue = findViewById(R.id.tue_txt_view);
        wed = findViewById(R.id.wed_txt_view);
        thu = findViewById(R.id.thu_txt_view);
        fri = findViewById(R.id.fri_txt_view);
        sat = findViewById(R.id.sat_txt_view);

        sun.setOnClickListener(this);
        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);
        sat.setOnClickListener(this);

        // set default repeat None
        if (dayMap.isEmpty()) {
            repeatView.setText("None");
        }

        // set default ringtone
        SharedPreference.setPreferenceData(AlarmRingtoneActivity.ALARM_RINGTONE_POSITION_TAG, 0);
        ringtoneNameView.setText(SharedPreference.getAllAudioFromDevice(getApplicationContext())
                .get(SharedPreference.getPreferenceDataInt(AlarmRingtoneActivity.ALARM_RINGTONE_POSITION_TAG))
                .getAudioName());

        // set default alert mode
        SharedPreference.setPreferenceData(AlertBottomSheetDialog.ALERT_MODE_TAG, "Ring");
        alertModeView.setText(SharedPreference.getPreferenceDataString(AlertBottomSheetDialog.ALERT_MODE_TAG));

        // set default snooze mode
        SharedPreference.setPreferenceData(SnoozeBottomSheetDialog.SNOOZE_MODE_TAG, "None");
        snoozeView.setText(SharedPreference.getPreferenceDataString(SnoozeBottomSheetDialog.SNOOZE_MODE_TAG));

        labelView.setText("None");
        // create calender for get current time
        Calendar calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        int mDay = calendar.get(Calendar.DAY_OF_WEEK);

        timePicker.setHour(mHour);
        timePicker.setMinute(mMinute);

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
                    saveReminder();
                }
            }
        });

        alertModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertBottomSheetDialog = new AlertBottomSheetDialog();
                alertBottomSheetDialog.show(getSupportFragmentManager(), ALERT_BOTTOM_SHEET_TAG);
            }
        });

        ringtoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddAlarmActivity.this, AlarmRingtoneActivity.class);
                startActivity(intent);
            }
        });

        snoozeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                snoozeBottomSheetDialog = new SnoozeBottomSheetDialog();
                snoozeBottomSheetDialog.show(getSupportFragmentManager(), SNOOZE_BOTTOM_SHEET_TAG);
            }
        });

        labelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LabelDialog labelDialog = new LabelDialog(labelView.getText().toString());
                labelDialog.show(getSupportFragmentManager(), "label dialog");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = SharedPreference.getAllAudioFromDevice(getApplicationContext())
                .get(SharedPreference.getPreferenceDataInt(AlarmRingtoneActivity.ALARM_RINGTONE_POSITION_TAG))
                .getAudioName().split("\\.")[0];
        ringtoneNameView.setText(name);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void OnModeClicked(String mode) {
        alertModeView.setText(mode);
    }

    @Override
    public void onClick(View view) {

        Button dayBtn = (Button) view;
        String day = dayBtn.getText().toString();
        if (dayBtn.isSelected()) {
            dayBtn.setSelected(false);
            switch (day) {
                case "Sun":
                    repeatView.setText(removeDaysInRepeatView(1));
                    break;
                case "Mon":
                    repeatView.setText(removeDaysInRepeatView(2));
                    break;
                case "Tue":
                    repeatView.setText(removeDaysInRepeatView(3));
                    break;
                case "Wed":
                    repeatView.setText(removeDaysInRepeatView(4));
                    break;
                case "Thu":
                    repeatView.setText(removeDaysInRepeatView(5));
                    break;
                case "Fri":
                    repeatView.setText(removeDaysInRepeatView(6));
                    break;
                case "Sat":
                    repeatView.setText(removeDaysInRepeatView(7));
                    break;
            }
        } else {
            dayBtn.setSelected(true);
            switch (day) {
                case "Sun":
                    repeatView.setText(addDaysInRepeatView(1, day));
                    break;
                case "Mon":
                    repeatView.setText(addDaysInRepeatView(2, day));
                    break;
                case "Tue":
                    repeatView.setText(addDaysInRepeatView(3, day));
                    break;
                case "Wed":
                    repeatView.setText(addDaysInRepeatView(4, day));
                    break;
                case "Thu":
                    repeatView.setText(addDaysInRepeatView(5, day));
                    break;
                case "Fri":
                    repeatView.setText(addDaysInRepeatView(6, day));
                    break;
                case "Sat":
                    repeatView.setText(addDaysInRepeatView(7, day));
                    break;
            }
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

        if (dayMap.isEmpty())
            mDays = "None";

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
    public void OnTimeClicked(String time) {
        snoozeView.setText(time);
    }

    @Override
    public void labelListener(String text) {
        if (text != null) {
            labelView.setText(text);
        }
    }

    public void saveReminder() {
        ReminderDataBase rd = new ReminderDataBase(getApplicationContext());

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

        if (mDays.equals("None"))
            mDays = "";

        int ID = rd.addReminder(new Reminder(mHour,
                mMinute,
                mAm_Pm,
                mSun,
                mMon,
                mTue,
                mWed,
                mThu,
                mFri,
                mSat,
                mDays,
                mAlertMode,
                mRingtone,
                mSnooze,
                mLabel,
                "true"));

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        // when you will fire the alarm current time before
        if (mCalendar.before(Calendar.getInstance())) {
            mCalendar.add(Calendar.DATE, 1);
        } else if (mSnooze.equals("5 Minutes"))
            mRepeatTime = 60000 * 5;

        else if (mSnooze.equals("10 Minutes"))
            mRepeatTime = 60000 * 10;

        else if (mSnooze.equals("15 Minutes"))
            mRepeatTime = 60000 * 15;

        else if (mSnooze.equals("30 Minutes"))
            mRepeatTime = 60000 * 30;

        else if (mSnooze.equals("1 Hour"))
            mRepeatTime = 60000 * 30 * 2;


        if (mSnooze.equals("None") &&
                !sun.isSelected() &&
                !mon.isSelected() &&
                !tue.isSelected() &&
                !wed.isSelected() &&
                !thu.isSelected() &&
                !fri.isSelected() &&
                !sat.isSelected())
            alarmReceiver.setAlarm(getApplicationContext(), mCalendar, ID);
        else
            alarmReceiver.setRepeatAlarm(getApplicationContext(), mCalendar, ID, mRepeatTime);


        // set Day Repeat
        if (sun.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), ID, 1, mHour, mMinute);
        else if (mon.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), ID, 2, mHour, mMinute);
        else if (tue.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), ID, 3, mHour, mMinute);
        else if (wed.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), ID, 4, mHour, mMinute);
        else if (thu.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), ID, 5, mHour, mMinute);
        else if (fri.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), ID, 6, mHour, mMinute);
        else if (sat.isSelected())
            alarmReceiver.setDayRepeatAlarm(getApplicationContext(), ID, 7, mHour, mMinute);

        onBackPressed();
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

    private boolean ifSameTimeIsThereInReminders(String timeStr) {

        ReminderDataBase db = new ReminderDataBase(getApplicationContext());
        List<Reminder> reminders = db.getAllReminders();
        ArrayList<String> rTimes = new ArrayList<>();
        int hour = 0;
        for (Reminder reminder : reminders) {
            hour = reminderHourFormat(reminder.getHour());
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

    public static int reminderHourFormat(int hour) {
        if (hour > 12)
            hour -= 12;
        else if (hour == 0)
            hour = 12;

        return hour;
    }

    public static String reminderMinuteFormat(int minute) {
        String min = "";
        if (minute < 10)
            min = "0" + minute;
        else
            min = String.valueOf(minute);

        return min;
    }
}