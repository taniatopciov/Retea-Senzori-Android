package com.example.retea_senzori_android.nodes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.models.SensorModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.Viewholder> {

    private List<SensorModel> sensorArrayList = new ArrayList<>();

    public SensorAdapter() {
    }

    @NonNull
    @Override
    public SensorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_card_fragment, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorAdapter.Viewholder holder, int position) {
        SensorModel model = sensorArrayList.get(position);
        holder.sensorName.setText(model.sensorType.toString());
    }

    @Override
    public int getItemCount() {
        return sensorArrayList.size();
    }

    public void setSensors(List<SensorModel> sensors) {
        this.sensorArrayList = sensors;
        if (sensors == null) {
            this.sensorArrayList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView sensorName;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            sensorName = itemView.findViewById(R.id.sensorName);
        }
    }
}
