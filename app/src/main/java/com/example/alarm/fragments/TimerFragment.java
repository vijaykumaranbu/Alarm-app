package com.example.alarm.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.alarm.R;
import com.example.alarm.activities.TimerRingtoneActivity;
import com.example.alarm.dataBase.SharedPreference;

import java.util.Locale;

public class TimerFragment extends Fragment {

    private NumberPicker hTimePicker,mTimePicker;
    private Button ssBtn;
    private TextView countDownView;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private boolean timerOnceOnPause = false;
    private long milliseconds;
    private int hourTP,minTP;
    private long endTimeInMillis;
    private int hour,min,sec;
    private static MediaPlayer mediaPlayer;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        TextView ringtoneText = view.findViewById(R.id.ringtoneTxt);
        hTimePicker = view.findViewById(R.id.hTimePicker);
        mTimePicker = view.findViewById(R.id.mTimePicker);
        ssBtn = view.findViewById(R.id.timerStartStopBtn);
        Button rBtn = view.findViewById(R.id.timerResetBtn);
        countDownView = view.findViewById(R.id.countDownView);

        hTimePicker.setMinValue(0);
        hTimePicker.setMaxValue(23);

        mTimePicker.setMinValue(0);
        mTimePicker.setMaxValue(59);

        countDownView.setVisibility(View.INVISIBLE);

        SharedPreference.setRingtoneList();

        ringtoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimerRingtoneActivity.class);
                startActivity(intent);
            }
        });

        hTimePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {

                setHour(newValue);
            }
        });

        mTimePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                
                setMin(newValue);
            }
        });

        //  start stop button here

        ssBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isRunning){
                    isRunning = false;
                    timerOnceOnPause = true;
                    stopTimer();
                }
                else {
                    if (hourTP == 0 && minTP == 0) {
                        Toast.makeText(getContext(),"Please set the timer",Toast.LENGTH_LONG).show();
                    }
                    else{
                        milliseconds = (hourTP * 60 * 1000 + minTP * 1000) * 60 + 1000;

                        hTimePicker.setVisibility(View.INVISIBLE);
                        mTimePicker.setVisibility(View.INVISIBLE);
                        countDownView.setVisibility(View.VISIBLE);

                        isRunning = true;
                        updateButton();

                        if (timerOnceOnPause) {
                            startTimer(endTimeInMillis);
                        } else {
                            startTimer(milliseconds);
                        }
                    }
                }
            }
        });

        //  Reset timer Button here

        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });



        return view;
    }

    private void startTimer(long milliseconds){

        countDownTimer = new CountDownTimer(milliseconds,1000) {
            @Override
            public void onTick(long timeInMilliseconds) {
                endTimeInMillis = timeInMilliseconds;
                hour = (int) (timeInMilliseconds / 1000) / 3600;
                min = (int) ((timeInMilliseconds / 1000) % 3600) / 60;
                sec = (int) (timeInMilliseconds / 1000) % 60;
                String format = String.format(Locale.getDefault(),"%02d:%02d:%02d",hour,min,sec);
                countDownView.setText(format);
            }

            @Override
            public void onFinish() {
                countDownView.setText("00:00:00");
                isRunning = false;

                try {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

                    alertDialog.setTitle("Timer");
                    alertDialog.setMessage("Time's Up");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(mediaPlayer.isPlaying() && mediaPlayer != null){
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }

                            // reset Timer


                            hour = (int) (getMilliseconds() / 1000) / 3600;
                            min = (int) ((getMilliseconds() / 1000) % 3600) / 60;
                            sec = (int) (getMilliseconds() / 1000) % 60;
                            String format = String.format(Locale.getDefault(),"%02d:%02d:%02d",hour,min,sec);
                            countDownView.setText(format);
                            timerOnceOnPause = false;
                            isRunning = false;
                            endTimeInMillis = 0L;
                            hour = 0;
                            min = 0;
                            sec = 0;
                            updateButton();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                    mediaPlayer = MediaPlayer.create(getContext(),
                            SharedPreference.ringtoneUriList.get(SharedPreference.getPreferenceDataInt("position")));
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void stopTimer(){
        countDownTimer.cancel();
        updateButton();

    }

    private void resetTimer(){
        try {
            countDownView.setVisibility(View.INVISIBLE);
            hTimePicker.setVisibility(View.VISIBLE);
            mTimePicker.setVisibility(View.VISIBLE);
            timerOnceOnPause = false;
            isRunning = false;
            endTimeInMillis = 0L;
            hourTP = 0;
            minTP = 0;
            hour = 0;
            min = 0;
            sec = 0;
            hTimePicker.setValue(0);
            mTimePicker.setValue(0);
            countDownTimer.cancel();
            updateButton();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateButton(){
        if(isRunning){
            ssBtn.setTextColor(getResources().getColor(R.color.Red));
            ssBtn.setText("Stop");
        }
        else{
            ssBtn.setTextColor(getResources().getColor(R.color.Green));
            ssBtn.setText("Start");
        }
    }

    private long getMilliseconds(){
        return milliseconds-1000;
    }

    public void setHour(int hour) {
        hourTP = hour;
    }

    public void setMin(int min){
        minTP = min;
    }
    }