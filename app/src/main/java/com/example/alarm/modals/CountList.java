package com.example.alarm.modals;

import android.content.Context;

public class CountList {
    private String stringCount;
    private String stringCountTime;
    private Context context;

    public CountList(Context context,String stringCount, String stringCountTime) {
        this.stringCount = stringCount;
        this.stringCountTime = stringCountTime;
        this.context = context;
    }

    public String getStringCount() {
        return stringCount;
    }

    public String getStringCountTime() {
        return stringCountTime;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
