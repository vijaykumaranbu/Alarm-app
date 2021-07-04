package com.example.alarm.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.R;
import com.example.alarm.adapters.RingtoneListAdapter;
import com.example.alarm.dataBase.SharedPreference;

public class TimerRingtoneActivity extends AppCompatActivity {

    private ListView ringtoneListView;
    private RingtoneListAdapter adapter;
    private ImageView back;
    public static MediaPlayer mediaPlayer;
    public int savedPosition;
    public static final String RINGTONE_POSITION_TAG = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone);

        ringtoneListView = findViewById(R.id.RingtoneListView);
        back = findViewById(R.id.back_ImageView);

        adapter = new RingtoneListAdapter(TimerRingtoneActivity.this, SharedPreference.ringtoneTitleList);
        ringtoneListView.setAdapter(adapter);

        savedPosition = SharedPreference.getPreferenceDataInt(RINGTONE_POSITION_TAG);
        setCheckedRadioButton(savedPosition);

        ringtoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SharedPreference.setPreferenceData(RINGTONE_POSITION_TAG, position);
                adapter.itemCheckChanged(position);
                try {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), SharedPreference.ringtoneUriList.get(position));
                    if (mediaPlayer != null)
                        mediaPlayer.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //  back to previous activity

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void setCheckedRadioButton(int position) {
        adapter.itemCheckChanged(position);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}