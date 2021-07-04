package com.example.alarm.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.alarm.R;
import com.example.alarm.adapters.ReminderAdapter;
import com.example.alarm.dialogs.DeleteAlarmBottomSheetDialog;
import com.example.alarm.fragments.AlarmFragment;
import com.example.alarm.fragments.StopWatchFragment;
import com.example.alarm.fragments.TimerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements DeleteAlarmBottomSheetDialog.ItemRemoveListener {

    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.clearAnimation();

        loadFragment(new AlarmFragment());

        //  Storage Permission

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                fragment = null;
                switch (item.getItemId()) {
                    case R.id.alarm:
                        fragment = new AlarmFragment();
                        break;
                    case R.id.stopWatch:
                        fragment = new StopWatchFragment();
                        break;
                    case R.id.timer:
                        fragment = new TimerFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }


    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutContainer, fragment)
                    .commit();
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (bottomNavigationView.getSelectedItemId() == R.id.alarm) {

            super.onBackPressed();
            finish();
        } else {
            bottomNavigationView.setSelectedItemId(R.id.alarm);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    // this method will be delete reminder item and recreate the fragment
    @Override
    public void onClickItemRemove(int position) {
        ReminderAdapter adapter = new ReminderAdapter(getApplicationContext());
        AlarmFragment alarmFragment = new AlarmFragment();
        adapter.deleteItem(position);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutContainer, alarmFragment)
                .commit();

        // if you want to pass any data we can use BUNDLE then we will receive data using onStart() method in fragment
    }

}