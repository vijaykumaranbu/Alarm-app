package com.example.alarm.modals;

public class ReminderItems{
    private final int mId;
    private final String mTime;
    private final String mTimeType;
    private final String mRepeatDays;
    private final String mLabel;
    private final Boolean mActive;

    public ReminderItems(int id, String time, String timeType, String repeatDays, String label, boolean active){
        mId = id;
        mTime = time;
        mTimeType = timeType;
        mRepeatDays = repeatDays;
        mLabel = label;
        mActive = active;
    }

    public String getmRepeatDays() {
        return mRepeatDays;
    }

    public String getmLabel() {
        return mLabel;
    }

    public int getmId() {
        return mId;
    }

    public String getmTimeType() {
        return mTimeType;
    }

    public String getmTime() {
        return mTime;
    }

    public boolean ismActive() {
        return mActive;
    }
}
