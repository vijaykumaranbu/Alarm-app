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

import com.example.alarm.R;
import com.example.alarm.dataBase.SharedPreference;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SnoozeBottomSheetDialog extends BottomSheetDialogFragment {

    private SnoozeBottomSheetListener listener;
    private RadioButton radioBtnNone,radioBtn5m,radioBtn10m,radioBtn15m,radioBtn30m,radioBtn1h;
    public static final String SNOOZE_MODE_TAG = "snooze_mode";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.snooze_bottom_sheet,container,false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RelativeLayout layoutNone = view.findViewById(R.id.layout_none);
        RelativeLayout layout5m = view.findViewById(R.id.layout_5m);
        RelativeLayout layout10m = view.findViewById(R.id.layout_10m);
        RelativeLayout layout15m = view.findViewById(R.id.layout_15m);
        RelativeLayout layout30m = view.findViewById(R.id.layout_30m);
        RelativeLayout layout1h = view.findViewById(R.id.layout_1h);

        radioBtnNone = view.findViewById(R.id.radio_btn_none);
        radioBtn5m = view.findViewById(R.id.radio_btn_5m);
        radioBtn10m = view.findViewById(R.id.radio_btn_10m);
        radioBtn15m = view.findViewById(R.id.radio_btn_15m);
        radioBtn30m = view.findViewById(R.id.radio_btn_30m);
        radioBtn1h = view.findViewById(R.id.radio_btn_1h);

        String snoozeMode = SharedPreference.getPreferenceDataString(SNOOZE_MODE_TAG);

        switch (snoozeMode) {
            case "None":
                radioBtnNone.setChecked(true);
                break;
            case "5 Minutes":
                radioBtn5m.setChecked(true);
                break;
            case "10 Minutes":
                radioBtn10m.setChecked(true);
                break;
            case "15 Minutes":
                radioBtn15m.setChecked(true);
                break;
            case "1 Hour":
                radioBtn1h.setChecked(true);
                break;
        }

        layoutNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioBtnNone.setChecked(true);
                radioBtn5m.setChecked(false);
                radioBtn10m.setChecked(false);
                radioBtn15m.setChecked(false);
                radioBtn30m.setChecked(false);
                radioBtn1h.setChecked(false);
                SharedPreference.setPreferenceData(SNOOZE_MODE_TAG, "None");
                listener.OnTimeClicked("None");
                dismiss();
            }
        });

        layout5m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioBtnNone.setChecked(false);
                radioBtn5m.setChecked(true);
                radioBtn10m.setChecked(false);
                radioBtn15m.setChecked(false);
                radioBtn30m.setChecked(false);
                radioBtn1h.setChecked(false);
                SharedPreference.setPreferenceData(SNOOZE_MODE_TAG, "5 Minutes");
                listener.OnTimeClicked("5 Minutes");
                dismiss();
            }
        });
        layout10m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioBtnNone.setChecked(false);
                radioBtn5m.setChecked(false);
                radioBtn10m.setChecked(true);
                radioBtn15m.setChecked(false);
                radioBtn30m.setChecked(false);
                radioBtn1h.setChecked(false);
                SharedPreference.setPreferenceData(SNOOZE_MODE_TAG, "10 Minutes");
                listener.OnTimeClicked("10 Minutes");
                dismiss();

            }
        });
        layout15m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioBtnNone.setChecked(false);
                radioBtn5m.setChecked(false);
                radioBtn10m.setChecked(false);
                radioBtn15m.setChecked(true);
                radioBtn30m.setChecked(false);
                radioBtn1h.setChecked(false);
                SharedPreference.setPreferenceData(SNOOZE_MODE_TAG, "15 Minutes");
                listener.OnTimeClicked("15 Minutes");
                dismiss();
            }
        });

        layout30m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioBtnNone.setChecked(false);
                radioBtn5m.setChecked(false);
                radioBtn10m.setChecked(false);
                radioBtn15m.setChecked(false);
                radioBtn30m.setChecked(true);
                radioBtn1h.setChecked(false);
                SharedPreference.setPreferenceData(SNOOZE_MODE_TAG, "30 Minutes");
                listener.OnTimeClicked("30 Minutes");
                dismiss();
            }
        });

        layout1h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioBtnNone.setChecked(false);
                radioBtn5m.setChecked(false);
                radioBtn10m.setChecked(false);
                radioBtn15m.setChecked(false);
                radioBtn30m.setChecked(false);
                radioBtn1h.setChecked(true);
                SharedPreference.setPreferenceData(SNOOZE_MODE_TAG, "1 Hour");
                listener.OnTimeClicked("1 Hour");
                dismiss();
            }
        });
    }

    public interface SnoozeBottomSheetListener{
        void OnTimeClicked(String time);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SnoozeBottomSheetListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(listener.toString()+"must implement interface");
        }
    }
    //
    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
