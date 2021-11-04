package com.example.dynamictest1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CricketerAdapter extends RecyclerView.Adapter<CricketerAdapter.CricketerView> {

    ArrayList<Cricketer> cricketers = new ArrayList<>();

    public CricketerAdapter(ArrayList<Cricketer> cricketers) {
        this.cricketers = cricketers;
    }

    @NonNull
    @Override
    public CricketerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cricketers,parent,false);
        return new CricketerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CricketerView holder, int position) {

        Cricketer cricketer = cricketers.get(position);
        Log.d("onBindViewHolder",cricketer.getCricketerName());
        Log.d("onBindViewHolder",cricketer.getTeamName());

        holder.textCricketerName.setText(cricketer.getCricketerName().toString());
        holder.textTeamName.setText(cricketer.getTeamName().toString());

    }

    @Override
    public int getItemCount() {
        return cricketers.size();
    }

    public class CricketerView extends  RecyclerView.ViewHolder{

        TextView textCricketerName,textTeamName;

        public CricketerView(@NonNull View itemView) {
            super(itemView);

            textCricketerName = (TextView)itemView.findViewById(R.id.text_cricketer_name);
            textTeamName = (TextView)itemView.findViewById(R.id.text_team_name);

        }

    }

}
