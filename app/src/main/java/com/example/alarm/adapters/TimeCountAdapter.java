package com.example.alarm.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alarm.modals.CountList;
import com.example.alarm.R;

import java.util.ArrayList;

public class TimeCountAdapter extends ArrayAdapter<CountList> {

    private Context context;

    public TimeCountAdapter(@NonNull Context context, int resource, ArrayList<CountList> Objects) {
        super(context, resource, Objects);
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String stringCount = getItem(position).getStringCount();
        String stringCountTime = getItem(position).getStringCountTime();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.count_items,parent,false);

        TextView textView1 = convertView.findViewById(R.id.textView1);
        TextView textView2 = convertView.findViewById(R.id.textView2);

        textView1.setText(stringCount);
        textView2.setText(stringCountTime);

        return convertView;
    }
}
