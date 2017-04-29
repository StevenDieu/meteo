package com.fges.meteo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fges.meteo.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by steven on 20/04/2017.
 */

public class InformationWeatherAdapter extends RecyclerView.Adapter<InformationWeatherAdapter.ViewHolder> {

    private ArrayList<Map<String,String>> inforamtionWeatherList;
    private Context context;

    public InformationWeatherAdapter(ArrayList<Map<String,String>> inforamtionWeatherList, Context context) {
        this.inforamtionWeatherList = inforamtionWeatherList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View wodView = inflater.inflate(R.layout.layout_information_weather,parent,false);
        return new ViewHolder(wodView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextTile.setText(inforamtionWeatherList.get(position).get("title"));
        holder.mTextDescription.setText(inforamtionWeatherList.get(position).get("description"));
    }

    @Override
    public int getItemCount() {
        return inforamtionWeatherList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTextTile;
        TextView mTextDescription;

        ViewHolder(View itemView) {
            super(itemView);
            mTextTile = (TextView) itemView.findViewById(R.id.text_title);
            mTextDescription = (TextView) itemView.findViewById(R.id.text_description);
        }
    }
}
