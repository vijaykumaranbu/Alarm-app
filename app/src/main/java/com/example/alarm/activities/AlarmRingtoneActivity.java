package com.example.alarm.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.R;
import com.example.alarm.adapters.SongListAdapter;
import com.example.alarm.dataBase.SharedPreference;

public class AlarmRingtoneActivity extends AppCompatActivity {

    private ListView listView;

    private SongListAdapter songListAdapter;
    public static MediaPlayer mediaPlayer;
    private ImageView back;
    public static final String ALARM_RINGTONE_POSITION_TAG = "audio_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringtone_list);

        listView = findViewById(R.id.RingtoneListView);
        back = findViewById(R.id.ringtone_back_ImageView);

        songListAdapter = new SongListAdapter(this, SharedPreference.getAllAudioFromDevice(getApplicationContext()));
        listView.setAdapter(songListAdapter);

        songListAdapter.radioButtonCheckChanged(SharedPreference.getPreferenceDataInt(ALARM_RINGTONE_POSITION_TAG));

        listView.setOnItemClickListener((adapterView, view, position, l) -> {

            SharedPreference.setPreferenceData(ALARM_RINGTONE_POSITION_TAG, position);
            songListAdapter.radioButtonCheckChanged(position);

            try {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                mediaPlayer = MediaPlayer.create(getApplicationContext(),
                        Uri.parse(SharedPreference.getAllAudioFromDevice(getApplicationContext()).get(position).getAudioPath()));

                if (mediaPlayer != null)
                    mediaPlayer.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        back.setOnClickListener(view -> onBackPressed());
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