package com.example.alarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.alarm.modals.AudioFiles;
import com.example.alarm.R;

import java.util.List;

public class SongListAdapter extends BaseAdapter {

    private final Context context;
    private final List<AudioFiles> audioList;
    private int radioButtonPosition = -1;

    public SongListAdapter(Context context, List<AudioFiles> audioList) {
        this.context = context;
        this.audioList = audioList;
    }

    @Override
    public int getCount() {
        return audioList.size();
    }

    @Override
    public Object getItem(int i) {
        return audioList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        SongViewHolder songViewHolder;
        if (view == null) {
            songViewHolder = new SongViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.audio_item_view, viewGroup, false);

            songViewHolder.songNameView = view.findViewById(R.id.audio_item_view);
            songViewHolder.songRadiobutton = view.findViewById(R.id.audio_item_radio_view);

            view.setTag(songViewHolder);

        } else {
            songViewHolder = (SongViewHolder) view.getTag();
        }

        songViewHolder.songNameView.setText(audioList.get(position).getAudioName().split("\\.")[0]);
        songViewHolder.songRadiobutton.setChecked(position == radioButtonPosition);

        songViewHolder.songNameView.setTag(position);
        songViewHolder.songRadiobutton.setTag(position);

        return view;
    }

    public void radioButtonCheckChanged(int position) {
        radioButtonPosition = position;
        notifyDataSetChanged();
    }


    static class SongViewHolder{

        TextView songNameView;
        RadioButton songRadiobutton;
        public SongViewHolder(){
        }
    }

}
