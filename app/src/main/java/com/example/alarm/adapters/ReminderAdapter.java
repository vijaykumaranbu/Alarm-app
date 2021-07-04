package com.example.alarm.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.activities.EditAlarmActivity;
import com.example.alarm.modals.ReminderItems;
import com.example.alarm.receivers.AlarmReceiver;
import com.example.alarm.dialogs.DeleteAlarmBottomSheetDialog;
import com.example.alarm.modals.Reminder;
import com.example.alarm.R;
import com.example.alarm.dataBase.ReminderDataBase;

import java.util.Calendar;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{

    private Context context;
    public static List<ReminderItems> items;
    private AlarmReceiver alarmReceiver = new AlarmReceiver();
    private EditAlarmActivity editAlarmActivity = new EditAlarmActivity();
    public static final String DELETE_BOTTOM_SHEET_TAG = "delete_item";
    private Calendar calendar;
    private Reminder reminder;
    private ReminderDataBase db;
    private Activity activity;

    public ReminderAdapter(Context context,Activity activity,List<ReminderItems> list) {
        this.context = context;
        this.activity = activity;
        items = list;
        db = new ReminderDataBase(context);
    }

    public ReminderAdapter(Context context){
        this.context = context;
    }

    public void deleteItem(int position){
        items.remove(position);
        notifyItemRangeChanged(position,getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_alarm_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReminderItems item = items.get(position);

        holder.mTimerTxt.setText(item.getmTime());
        holder.mTimeTypeTxt.setText(item.getmTimeType());
        holder.mSwitchBtn.setChecked(item.ismActive());

        try {
            if(item.getmRepeatDays().isEmpty())
                holder.daysView.setVisibility(View.GONE);
            else
                holder.labelView.setText(item.getmLabel());
        }catch (NullPointerException e){
            holder.daysView.setVisibility(View.GONE);
        }

        if(item.getmLabel().isEmpty()){
            holder.labelView.setVisibility(View.GONE);
        }else {
            holder.daysView.setText(item.getmRepeatDays());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    int id = item.getmId();
                    Intent editIntent = new Intent(context, EditAlarmActivity.class);
                    editIntent.putExtra(EditAlarmActivity.REMINDER_ID, String.valueOf(id));
                    context.startActivity(editIntent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                DeleteAlarmBottomSheetDialog.newInstance(context,item.getmId(),item.getmTime() + " " + item.getmTimeType(),position)
                        .show(((AppCompatActivity)context).getSupportFragmentManager(),DELETE_BOTTOM_SHEET_TAG);
                return true;
            }
        });

        holder.mSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        if(holder.mSwitchBtn.isChecked()){
                            setAlarm(item.getmId());
                        }
                        else{
                            alarmReceiver.cancelAlarm(context,item.getmId());
                            reminder = db.getReminder(item.getmId());
                            reminder.setActive("false");
                            db.updateReminder(reminder);
                            Toast.makeText(context,"Alarm Cancel",Toast.LENGTH_SHORT).show();
                        }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTimerTxt;
        private final TextView mTimeTypeTxt;
        private final SwitchCompat mSwitchBtn;
        private final TextView labelView;
        private final TextView daysView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTimerTxt = itemView.findViewById(R.id.timerItemView);
            mTimeTypeTxt = itemView.findViewById(R.id.alarm_time_type);
            mSwitchBtn = itemView.findViewById(R.id.alarmSwitch);
            labelView = itemView.findViewById(R.id.reminder_label_view);
            daysView = itemView.findViewById(R.id.reminder_day_list_view);
        }
    }

    private void setAlarm(int id){

        reminder = db.getReminder(id);
        if(reminder != null){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,reminder.getHour());
            calendar.set(Calendar.MINUTE,reminder.getMinute());
            calendar.set(Calendar.SECOND,0);

            if(calendar.before(Calendar.getInstance())){
                calendar.add(Calendar.DATE,1);
            }

            String snooze = reminder.getSnooze();
            long repeatTime = 0;

            if(snooze.equals("5 Minutes"))
                repeatTime = 60000 * 5;

            else if(snooze.equals("10 Minutes"))
                repeatTime = 60000 * 10;

            else if(snooze.equals("15 Minutes"))
                repeatTime = 60000 * 15;

            else if(snooze.equals("30 Minutes"))
                repeatTime = 60000 * 30;

            else if(snooze.equals("1 Hour"))
                repeatTime = 60000 * 30 * 2;



            if(snooze.equals("None")) {
                alarmReceiver.setAlarm(context, calendar, id);
            }
            else {
                alarmReceiver.setRepeatAlarm(context, calendar, id, repeatTime);
            }

            if(Boolean.parseBoolean(reminder.getSun()))
                alarmReceiver.setDayRepeatAlarm(context,id,1,reminder.getHour(),reminder.getMinute());
            else if(Boolean.parseBoolean(reminder.getMon()))
                alarmReceiver.setDayRepeatAlarm(context,id,2,reminder.getHour(),reminder.getMinute());
            else if(Boolean.parseBoolean(reminder.getTue()))
                alarmReceiver.setDayRepeatAlarm(context,id,3,reminder.getHour(),reminder.getMinute());
            else if(Boolean.parseBoolean(reminder.getWed()))
                alarmReceiver.setDayRepeatAlarm(context,id,4,reminder.getHour(),reminder.getMinute());
            else if(Boolean.parseBoolean(reminder.getThu()))
                alarmReceiver.setDayRepeatAlarm(context,id,5,reminder.getHour(),reminder.getMinute());
            else if(Boolean.parseBoolean(reminder.getFri()))
                alarmReceiver.setDayRepeatAlarm(context,id,6,reminder.getHour(),reminder.getMinute());
            else if(Boolean.parseBoolean(reminder.getSat()))
                alarmReceiver.setDayRepeatAlarm(context,id,7,reminder.getHour(),reminder.getMinute());

            reminder.setActive("true");
            db.updateReminder(reminder);

        }
    }

}

