package com.example.alarm.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import com.example.alarm.activities.AddAlarmActivity;
import com.example.alarm.activities.WakeUpActivity;
import com.example.alarm.dataBase.ReminderDataBase;
import com.example.alarm.dataBase.SharedPreference;
import com.example.alarm.modals.Reminder;

import java.util.Calendar;

import static com.example.alarm.activities.EditAlarmActivity.REMINDER_ID;

public class AlarmReceiver extends BroadcastReceiver {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public static PowerManager.WakeLock wakeLock;
    private static final String WAKELOCK_TAG = "Alarm : WakeLocker";

    @Override
    public void onReceive(Context context, Intent intent) {
        int mReceivedId =Integer.parseInt(intent.getStringExtra(REMINDER_ID));

        ReminderDataBase rb = new ReminderDataBase(context);
        Reminder reminder = rb.getReminder(mReceivedId);
        String mTime = AddAlarmActivity.reminderHourFormat(reminder.getHour()) + ":"
                + AddAlarmActivity.reminderMinuteFormat(reminder.getMinute());

        String uri = SharedPreference.getAllAudioFromDevice(context).get(reminder.getRingtone()).getAudioPath();

        wakeLocker(context); // its will be wake up the device when device is sleeping

        Intent alarmScrIntent = new Intent(context, WakeUpActivity.class);
        alarmScrIntent.putExtra(WakeUpActivity.ALARM_ID,String.valueOf(mReceivedId));
        alarmScrIntent.putExtra(WakeUpActivity.ALARM_TIME,mTime);
        alarmScrIntent.putExtra(WakeUpActivity.ALARM_AM_PM,reminder.getAm_pm());
        alarmScrIntent.putExtra(WakeUpActivity.ALARM_URI,uri);
        context.startActivity(alarmScrIntent);

    }

    public void setAlarm(Context context, Calendar calendar,int ID){

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent mIntent = new Intent(context,AlarmReceiver.class);
        mIntent.putExtra(REMINDER_ID,String.valueOf(ID));
        pendingIntent = PendingIntent.getBroadcast(context,ID,mIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        long currentTime = cal.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

        // Toast
        showReminderTimeToast(diffTime,context);
    }

    public void setRepeatAlarm(Context context,Calendar calendar,int ID,long repeatTime){

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.putExtra(REMINDER_ID,String.valueOf(ID));
        pendingIntent = PendingIntent.getBroadcast(context,ID,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        long currentTime = cal.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),repeatTime,pendingIntent);

        showReminderTimeToast(diffTime,context);
    }

    public void setDayRepeatAlarm(Context context,int ID,int dayOfWeek,int hour,int minute){
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.putExtra(REMINDER_ID,String.valueOf(ID));
        pendingIntent = PendingIntent.getBroadcast(context,ID,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,0);
        if (cal.before(Calendar.getInstance())) {
            cal.add(Calendar.DATE,7);
        }
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),7*24*60*60*1000,pendingIntent);

        Toast.makeText(context,"Every " + WakeUpActivity.DAY[dayOfWeek-1] + ", Alarm will Fire",Toast.LENGTH_LONG).show();
        }

    public void cancelAlarm(Context context,int ID){
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(context,ID,new Intent(context,AlarmReceiver.class),0);
        alarmManager.cancel(pendingIntent);
    }

    private void showReminderTimeToast(long diffTime,Context context){
        int hour = (int) (diffTime / 1000) / 3600;
        int minute = (int) ((diffTime / 1000) % 3600) / 60 + 1;

        if(minute == 60) {
            hour += 1;
            Toast.makeText(context, "The Alarm will remind in " + hour + " hours ", Toast.LENGTH_LONG).show();
        }
        else if(hour > 0){
            Toast.makeText(context, "The Alarm will remind in " + hour + " hours " + minute + " minutes", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "The Alarm will remind in " + minute + " minutes", Toast.LENGTH_LONG).show();
        }
    }

    public void wakeLocker(Context context){

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                |PowerManager.ACQUIRE_CAUSES_WAKEUP
                |PowerManager.ON_AFTER_RELEASE,WAKELOCK_TAG);
        wakeLock.acquire(30*60*1000L /*30 minutes*/);
    }
}
