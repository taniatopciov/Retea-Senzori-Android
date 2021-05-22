package com.example.retea_senzori_android.nodes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.models.SensorModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.Viewholder> {

    private Context context;
    private ArrayList<SensorModel> sensorArrayList;

    public SensorAdapter(Context context, ArrayList<SensorModel> sensorArrayList) {
        this.context = context;
        this.sensorArrayList = sensorArrayList;
    }

    @NonNull
    @Override
    public SensorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_card_fragment, parent, false);
        return new SensorAdapter.Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SensorAdapter.Viewholder holder, int position) {
        SensorModel model = sensorArrayList.get(position);
        holder.sensorName.setText(model.sensorType);
    }

    @Override
    public int getItemCount() {
        return sensorArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView sensorName;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            sensorName = itemView.findViewById(R.id.sensorName);
        }
    }
}
