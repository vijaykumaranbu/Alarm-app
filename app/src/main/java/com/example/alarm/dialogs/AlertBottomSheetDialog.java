package com.example.alarm.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alarm.dataBase.SharedPreference;
import com.example.alarm.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AlertBottomSheetDialog extends BottomSheetDialogFragment {

    private AlertBottomSheetListener listener;
    private RadioButton ringRadioBtn,ringVibrateRadioBtn,vibrateRadioBtn;
    public static final String ALERT_MODE_TAG = "alert_mode";

    public AlertBottomSheetDialog(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.alert_bottom_sheet_layout,container,false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RelativeLayout ringLayout = view.findViewById(R.id.ring_layout);
        RelativeLayout ringVibrateLayout = view.findViewById(R.id.ring_vibrate_layout);
        RelativeLayout vibrateLayout = view.findViewById(R.id.vibrate_layout);

        ringRadioBtn = view.findViewById(R.id.ring_radio_btn);
        ringVibrateRadioBtn = view.findViewById(R.id.ring_vibrate_radio_btn);
        vibrateRadioBtn = view.findViewById(R.id.vibrate_radio_btn);

        String alertMode = SharedPreference.getPreferenceDataString(ALERT_MODE_TAG);

        if(alertMode.equals("Ring")){
            ringRadioBtn.setChecked(true);
        }
        else if(alertMode.equals("Ring & Vibrate")){
            ringVibrateRadioBtn.setChecked(true);
        }else {
            vibrateRadioBtn.setChecked(true);
        }

        ringLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringRadioBtn.setChecked(true);
                ringVibrateRadioBtn.setChecked(false);
                vibrateRadioBtn.setChecked(false);
                SharedPreference.setPreferenceData(ALERT_MODE_TAG,"Ring");
                listener.OnModeClicked("Ring");
                dismiss();
            }
        });
        ringVibrateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringRadioBtn.setChecked(false);
                ringVibrateRadioBtn.setChecked(true);
                vibrateRadioBtn.setChecked(false);
                SharedPreference.setPreferenceData(ALERT_MODE_TAG,"Ring & Vibrate");
                listener.OnModeClicked("Ring & Vibrate");
                dismiss();
            }
        });
        vibrateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringRadioBtn.setChecked(false);
                ringVibrateRadioBtn.setChecked(false);
                vibrateRadioBtn.setChecked(true);
                SharedPreference.setPreferenceData(ALERT_MODE_TAG,"Vibrate");
                listener.OnModeClicked("Vibrate");
                dismiss();
            }
        });
    }


    public interface AlertBottomSheetListener{
        void OnModeClicked(String mode);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AlertBottomSheetListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(listener.toString()+"must implement interface");
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();

    }
}
