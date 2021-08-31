
package com.example.alarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.alarm.R;

import java.util.List;


public class RingtoneListAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> ringtoneTitleList;
    private int selectedPosition = -1;

    public RingtoneListAdapter(Context context, List<String> ringtoneTitleList) {
        this.context = context;
        this.ringtoneTitleList = ringtoneTitleList;
    }

    @Override
    public int getCount() {
        return ringtoneTitleList.size();
    }

    @Override
    public Object getItem(int i) {
        return ringtoneTitleList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.ringtone_item_view, viewGroup, false);

            viewHolder.label = view.findViewById(R.id.label);
            viewHolder.radioButton = view.findViewById(R.id.radioButton);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
            viewHolder.label.setText(ringtoneTitleList.get(i));
            viewHolder.radioButton.setChecked(i == selectedPosition);

            viewHolder.label.setTag(i);
            viewHolder.radioButton.setTag(i);

            return view;
        }

        public void itemCheckChanged (int position){
            selectedPosition = position;
            notifyDataSetChanged();
        }

    static class ViewHolder{
        TextView label;
        RadioButton radioButton;
        ViewHolder(){

        }
    }
}