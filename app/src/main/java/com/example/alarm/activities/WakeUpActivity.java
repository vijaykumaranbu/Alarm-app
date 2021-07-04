package com.example.alarm.activities;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.R;
import com.example.alarm.dataBase.ReminderDataBase;
import com.example.alarm.modals.Reminder;
import com.example.alarm.receivers.AlarmReceiver;

import java.util.Calendar;

import javax.xml.validation.Validator;

public class WakeUpActivity extends AppCompatActivity {

    public static final String ALARM_ID = "alarm_id";
    public static final String ALARM_TIME = "alarm_time";
    public static final String ALARM_URI = "alarm_uri";
    public static final String ALARM_AM_PM = "alarm_am_pm";
    private TextView timeView, calenderView, amOrPmView;
    private Button remindLaterBtn, alarmOffBtn;
    private static MediaPlayer mediaPlayer;
    private ReminderDataBase db;
    private Reminder reminder;
    private Calendar calendar;
    private Vibrator vibrator;
    private boolean stopVibrate = false;
    private AlarmReceiver alarmReceiver = new AlarmReceiver();
    private String time, amOrPm, uri;
    private String[] MONTH = {"January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"};
    public static String[] DAY = {"Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"};
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        timeView = findViewById(R.id.time_view_in_alarm_screen);
        calenderView = findViewById(R.id.date_moth_day_view);
        amOrPmView = findViewById(R.id.am_pm_alarm_screen);
        remindLaterBtn = findViewById(R.id.remind_later_btn);
        alarmOffBtn = findViewById(R.id.alarm_off_btn);

        // it will show this activity when wakelock is fired
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        id = Integer.parseInt(getIntent().getStringExtra(ALARM_ID));
        time = getIntent().getStringExtra(ALARM_TIME);
        amOrPm = getIntent().getStringExtra(ALARM_AM_PM);
        uri = getIntent().getStringExtra(ALARM_URI);

        db = new ReminderDataBase(getApplicationContext());
        reminder = db.getReminder(id);

        calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String dateFormat = "" + MONTH[month] + " " + date + "  " + DAY[day - 1];
        calenderView.setText(dateFormat);
        // set button visible
        if (reminder.getSnooze().equalsIgnoreCase("none"))
            remindLaterBtn.setVisibility(View.GONE);
        else
            alarmOffBtn.setVisibility(View.GONE);

        // set intent data in TextView
        timeView.setText(time);
        amOrPmView.setText(amOrPm);

        startMediaPlayer(); // start play the song

        alarmOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();

                    if (reminder.getSnooze().equalsIgnoreCase("none")) {
                        alarmReceiver.cancelAlarm(getApplicationContext(), id);
                        reminder.setActive("false");
                        db.updateReminder(reminder);
                    }
                    Toast.makeText(getApplicationContext(), "Alarm Off", Toast.LENGTH_LONG).show();
                    stopVibrate = true;
                }
                finish();
            }
        });
        remindLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    Toast.makeText(getApplicationContext(), "Snoozing for " + reminder.getSnooze(), Toast.LENGTH_LONG).show();
                    stopVibrate = true;
                }
                finish();
            }
        });
    }

    private void startMediaPlayer() {

        if (db.getReminder(id).getAlertMode().equalsIgnoreCase("ring")) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(uri));
            if (mediaPlayer != null) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        } else if (db.getReminder(id).getAlertMode().equalsIgnoreCase("vibrate")) {
            startVibrate();
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(uri));
            if (mediaPlayer != null) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
            startVibrate();
        }

    }

    private void startVibrate(){
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i <= 200; i++){
                    if(stopVibrate) {
                        vibrator.cancel();
                        return;
                    }
                    vibrator.vibrate(1000);
                    SystemClock.sleep(3000);
                }
            }
        }).start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        AlarmReceiver.wakeLock.release();
    }

}