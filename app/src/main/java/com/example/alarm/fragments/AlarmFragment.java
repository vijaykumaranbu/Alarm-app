package com.example.alarm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.R;
import com.example.alarm.activities.AddAlarmActivity;
import com.example.alarm.adapters.ReminderAdapter;
import com.example.alarm.dataBase.ReminderDataBase;
import com.example.alarm.modals.Reminder;
import com.example.alarm.modals.ReminderItems;

import java.util.ArrayList;
import java.util.List;


public class AlarmFragment extends Fragment {

    private ReminderAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noAlarmTxt;
    private ReminderDataBase db;

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alam, container, false);

        Toolbar toolbar = view.findViewById(R.id.AlarmToolBar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.alarm_recycler_view);
        noAlarmTxt = view.findViewById(R.id.no_alarm);

        db = new ReminderDataBase(getContext());
        List<ReminderItems> mItemList = getData();
        List<Reminder> mList = db.getAllReminders();
        if(mList.isEmpty()){
            noAlarmTxt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            noAlarmTxt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ReminderAdapter(getContext(),activity, mItemList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);   // You should put the line here, when you use menu in fragment
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.alarm_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.addAlarmMenu) {
            Intent intent = new Intent(getActivity(), AddAlarmActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public List<ReminderItems> getData(){
        ArrayList<ReminderItems> items = new ArrayList<>();
        ReminderDataBase db = new ReminderDataBase(getContext());
        List<Reminder> reminders = db.getAllReminders();

        List<Integer> id = new ArrayList<>();
        List<String> time = new ArrayList<>();
        List<String> timeType = new ArrayList<>();
        List<String> repeatDays = new ArrayList<>();
        List<String> label = new ArrayList<>();
        List<Boolean> active = new ArrayList<>();
        int i = 0;
        for (Reminder r : reminders){
            id.add(r.getID());
            time.add(AddAlarmActivity.reminderHourFormat(r.getHour()) + ":"
                    + AddAlarmActivity.reminderMinuteFormat(r.getMinute()));
            timeType.add((r.getAm_pm()));
            repeatDays.add(r.getDays());
            label.add(r.getLabel());
            active.add(Boolean.parseBoolean(r.getActive()));
            items.add(new ReminderItems(id.get(i),time.get(i),timeType.get(i),repeatDays.get(i),label.get(i),active.get(i)));
            i++;
        }
        return items;
    }

    @Override
    public void onResume() {
        super.onResume();
        db = new ReminderDataBase(getContext());
        List<Reminder> mList = db.getAllReminders();
        if(mList.isEmpty()){
            noAlarmTxt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            noAlarmTxt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new ReminderAdapter(getContext(),getActivity(),getData());
        recyclerView.setAdapter(adapter);
    }
}
