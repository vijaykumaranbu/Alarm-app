package com.example.alarm.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alarm.adapters.ReminderAdapter;
import com.example.alarm.receivers.AlarmReceiver;
import com.example.alarm.dataBase.ReminderDataBase;
import com.example.alarm.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DeleteAlarmBottomSheetDialog extends BottomSheetDialogFragment{

    public static final String DELETE_BUNDLE_ID_TAG = "bottom_bundle_id";
    public static final String DELETE_BUNDLE_TIME_TAG = "bottom_bundle_time";
    public static final String DELETE_BUNDLE_POSITION_TAG = "bottom_bundle_position";
    private ItemRemoveListener listener;
    private LinearLayout delete;
    private TextView deleteTimeTitle;
    private ReminderDataBase dataBase;
    private ReminderAdapter adapter;
    private AlarmReceiver receiver;
    private Context context;

    DeleteAlarmBottomSheetDialog(Context context){this.context = context;}

    public static DeleteAlarmBottomSheetDialog newInstance(Context context, int ID, String time, int position){
        DeleteAlarmBottomSheetDialog deleteAlarmBottomSheet = new DeleteAlarmBottomSheetDialog(context);
        Bundle bundle = new Bundle();
        bundle.putInt(DELETE_BUNDLE_ID_TAG,ID);
        bundle.putString(DELETE_BUNDLE_TIME_TAG,time);
        bundle.putInt(DELETE_BUNDLE_POSITION_TAG,position);
        deleteAlarmBottomSheet.setArguments(bundle);
        return deleteAlarmBottomSheet;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_alarm_bottom_sheet,container,false);

        delete = view.findViewById(R.id.delete_alarm);
        deleteTimeTitle = view.findViewById(R.id.delete_bottom_sheet_text);

        dataBase = new ReminderDataBase(context);
        adapter = new ReminderAdapter(context);
        receiver = new AlarmReceiver();

        String timeTitle = getArguments().getString(DELETE_BUNDLE_TIME_TAG);
        deleteTimeTitle.setText(timeTitle);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert getArguments() != null;
                int id = getArguments().getInt(DELETE_BUNDLE_ID_TAG);
                int position = getArguments().getInt(DELETE_BUNDLE_POSITION_TAG);
                receiver.cancelAlarm(context,id);
                dataBase.deleteReminder(id);
                listener.onClickItemRemove(position);
                dismiss();
            }
        });
        return view;
    }

    public interface ItemRemoveListener{
         void onClickItemRemove(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
                listener = (ItemRemoveListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement this class");
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();

    }
}
