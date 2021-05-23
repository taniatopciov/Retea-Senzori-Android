package com.example.retea_senzori_android.nodes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.nodes.factory.Sensor;
import com.example.retea_senzori_android.utils.UIRunner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.Viewholder> {

    private List<Sensor> sensorArrayList = new ArrayList<>();
    private final UIRunner uiRunner;

    public SensorAdapter(UIRunner uiRunner) {
        this.uiRunner = uiRunner;
    }

    @NonNull
    @Override
    public SensorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_card_fragment, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorAdapter.Viewholder holder, int position) {
        System.out.println("Sensor Updated");
        Sensor model = sensorArrayList.get(position);
        holder.sensorName.setText(model.getSensorModel().toString());
        holder.itemView.setOnClickListener(v -> Navigation.findNavController(v).navigate(NodeDetailsFragmentDirections.navigateToSensorFragment(model.getSensorModel())));
        model.subscribe(value -> {
            uiRunner.run(() -> {
                holder.sensorLiveValue.setText(String.valueOf(value));
            });
        });        
    }

    @Override
    public int getItemCount() {
        return sensorArrayList.size();
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensorArrayList = sensors;
        if (sensors == null) {
            this.sensorArrayList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView sensorName;
        private final TextView sensorLiveValue;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            sensorLiveValue = itemView.findViewById(R.id.sensorDataValue);
            sensorName = itemView.findViewById(R.id.sensorType);
        }
    }
}
