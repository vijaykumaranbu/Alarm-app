package com.example.alarm.dataBase;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import com.example.alarm.modals.AudioFiles;

import java.util.ArrayList;
import java.util.List;

public class SharedPreference extends android.app.Application{

    private static Application instance;
    private static SharedPreferences preferences;
    public static List<Uri> ringtoneUriList;
    public static List<String> ringtoneTitleList;
    public static Cursor ringtoneCursor;

    public SharedPreference(){}

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static SharedPreferences getSharedPreferences(){
        if(preferences == null){
            preferences = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(instance);
        }
        return preferences;
    }

    // setPreferenceData

    public static void setPreferenceData(String key,String value){
        getSharedPreferences().edit().putString(key,value).apply();
    }

    public static void setPreferenceData(String key,int value){
        getSharedPreferences().edit().putInt(key,value).apply();
    }

    // getPreferenceData

    public static String getPreferenceDataString(String key){
        return getSharedPreferences().getString(key,"");
    }

    public static int getPreferenceDataInt(String key){
        return getSharedPreferences().getInt(key,0);
    }

    public static void setRingtoneList(){
        ringtoneUriList = new ArrayList<>();
        ringtoneTitleList = new ArrayList<>();

        try{
            RingtoneManager ringtoneManager = new RingtoneManager(instance);
            ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);

            ringtoneCursor = ringtoneManager.getCursor();
            int ringtoneCount = ringtoneCursor.getCount();

            if(ringtoneCount == 0 && !ringtoneCursor.moveToFirst()){
                ringtoneCursor.close();
            }

            while(!ringtoneCursor.isAfterLast() && ringtoneCursor.moveToNext()){
                int currentPosition = ringtoneCursor.getPosition();
                String ringtoneTitle = ringtoneCursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                ringtoneTitleList.add(ringtoneTitle);
                Uri ringtoneUri = ringtoneManager.getRingtoneUri(currentPosition);
                ringtoneUriList.add(ringtoneUri);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<AudioFiles> getAllAudioFromDevice(Context context){

        List<AudioFiles> audioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);

        if(cursor != null){
            while (cursor.moveToNext()){

                AudioFiles audioFiles = new AudioFiles();
                String path = cursor.getString(0);
                String name = path.substring(path.lastIndexOf("/")+1);

                audioFiles.setAudioName(name);
                audioFiles.setAudioPath(path);

                Log.e("Name : " + name,"Path : " + path);

                audioList.add(audioFiles);
            }
            cursor.close();
        }

        return audioList;
    }
}
