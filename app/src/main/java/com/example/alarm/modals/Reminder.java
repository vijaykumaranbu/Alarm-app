package com.example.alarm.modals;

public class Reminder {

    private int ID;
    private int hour;
    private int minute;
    private String am_pm;
    private String sun;
    private String mon;
    private String tue;
    private String wed;
    private String thu;
    private String fri;
    private String sat;
    private String days;
    private String alertMode;
    private int ringtone;
    private String snooze;
    private String label;
    private String active;

    public Reminder(int ID,
                    int hour,
                    int minute,
                    String am_pm,
                    String sun,
                    String mon,
                    String tue,
                    String wed,
                    String thu,
                    String fri,
                    String sat,
                    String days,
                    String alertMode,
                    int ringtone,
                    String snooze,
                    String label,
                    String active) {
        this.ID = ID;
        this.hour = hour;
        this.minute = minute;
        this.am_pm = am_pm;
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.days = days;
        this.alertMode = alertMode;
        this.ringtone = ringtone;
        this.snooze = snooze;
        this.label = label;
        this.active = active;
    }

    public Reminder(int hour,
                    int minute,
                    String am_pm,
                    String sun,
                    String mon,
                    String tue,
                    String wed,
                    String thu,
                    String fri,
                    String sat,
                    String days,
                    String alertMode,
                    int ringtone,
                    String snooze,
                    String label,
                    String active) {
        this.hour = hour;
        this.minute = minute;
        this.am_pm = am_pm;
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.days = days;
        this.alertMode = alertMode;
        this.ringtone = ringtone;
        this.snooze = snooze;
        this.label = label;
        this.active = active;
    }

    public Reminder(){}


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getTue() {
        return tue;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public String getWed() {
        return wed;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getFri() {
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getAlertMode() {
        return alertMode;
    }

    public void setAlertMode(String alertMode) {
        this.alertMode = alertMode;
    }

    public int getRingtone() {
        return ringtone;
    }

    public void setRingtone(int ringtone) {
        this.ringtone = ringtone;
    }

    public String getSnooze() {
        return snooze;
    }

    public void setSnooze(String snooze) {
        this.snooze = snooze;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}

