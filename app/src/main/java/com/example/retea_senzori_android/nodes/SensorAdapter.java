package com.example.retea_senzori_android.nodes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.nodes.factory.Sensor;
import com.example.retea_senzori_android.nodes.factory.SensorFactory;
import com.example.retea_senzori_android.nodes.factory.SensorValueDisplayer;
import com.example.retea_senzori_android.utils.runners.UIRunner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.Viewholder> {

    private List<Sensor> sensorArrayList = new ArrayList<>();
    private final UIRunner uiRunner;
    private String nodeLogId = "";

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
        Sensor sensor = sensorArrayList.get(position);
        holder.sensorName.setText(sensor.getSensorType().toString());
        holder.itemView.setOnClickListener(v -> Navigation.findNavController(v).navigate(NodeDetailsFragmentDirections.navigateToSensorFragment(sensor.getSensorModel(), nodeLogId == null ? "" : nodeLogId)));

        SensorValueDisplayer sensorValueDisplayer = SensorFactory.getSensorValueMapper(sensor.getSensorType());

        sensor.subscribe(value ->
                uiRunner.run(() ->
                        holder.sensorLiveValue.setText(sensorValueDisplayer.display(value))));
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

    public void setNodeLogId(String nodeLogId) {
        this.nodeLogId = nodeLogId;
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
