package com.example.alarm.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.alarm.R;
import com.example.alarm.adapters.TimeCountAdapter;
import com.example.alarm.modals.CountList;

import java.util.ArrayList;
import java.util.Locale;


public class StopWatchFragment extends Fragment{

    private Button stopStartBtn,countResetBtn;
    private TextView timerView;
    private Double time = 0.0;
    private boolean isRunning = false;
    private ListView countView;
    private int count = 0;
    private ArrayList<CountList> list;
    private TimeCountAdapter adapter;
    private Handler handler;
    private Runnable runnable;

    public StopWatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_stop_watch, container, false);

        stopStartBtn = view.findViewById(R.id.stopStartBtn);
        countResetBtn = view.findViewById(R.id.countResetBtn);
        timerView = view.findViewById(R.id.timerView);
        countView = view.findViewById(R.id.listOfCount);

        list = new ArrayList<>();
        adapter = new TimeCountAdapter(getContext(),R.layout.count_items,list);

        handler = new Handler();

        stopStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRunning){
                    startTime();
                    handler.postDelayed(runnable,0);
                    isRunning = true;
                    stopStartBtn.setText("Stop");
                    stopStartBtn.setTextColor(getResources().getColor(R.color.Red));
                    countResetBtn.setText("Count");

                }
                else{
                    handler.removeCallbacks(runnable);
                    isRunning = false;
                    stopStartBtn.setText("Start");
                    stopStartBtn.setTextColor(getResources().getColor(R.color.Green));
                    countResetBtn.setText("Reset");

                }
            }
        });

        countResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    timerView.setText("00:00.0");
                    time = 0.0;
                    count = 0;
                    list.clear();
                } else {
                      setCountView();
                }
            }
        });

        return view;
    }


    private void startTime(){
        runnable = new Runnable() {
            @Override
            public void run() {

                time++;

                int rounded = (int) Math.round(time);

                int seconds = ((rounded % 86400) % 3600) % 60 / 6;
                int minutes = ((rounded % 86400) % 3600) / 60;
                int hours = (rounded % 86400) / 3600;

                timerView.setText(String.format(Locale.getDefault(),"%02d:%02d.%d",hours,minutes,seconds));
                handler.postDelayed(runnable,10);

            }
        };
    }

    private void setCountView(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        count += 1;
                        String stringCount = "Count " + count;
                        String stringCountTime = timerView.getText().toString();
                        CountList countList = new CountList(getContext(), stringCount, stringCountTime);
                        list.add(countList);
                        countView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

}