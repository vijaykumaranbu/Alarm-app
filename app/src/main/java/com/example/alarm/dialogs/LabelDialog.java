package com.example.alarm.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.alarm.R;

public class LabelDialog extends AppCompatDialogFragment {

    private EditText label;
    private LabelDialogListener listener;
    private String labelText;

    public LabelDialog(String labelText){
        this.labelText = labelText;
    }

    public LabelDialog(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.label_dialog_layout,null);

        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("Label")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = label.getText().toString();
                        listener.labelListener(text);
                    }
                });
        label = view.findViewById(R.id.label_dialog_text);
        if(labelText.equalsIgnoreCase("none")){
            label.setText("");
        }else {
            label.setText(labelText);
        }
        return builder.create();
    }

    public interface LabelDialogListener{
        void labelListener(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (LabelDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement LabelDialogListener interface");
        }
    }
}
