package com.example.retea_senzori_android.nodes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.models.NodeModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.Viewholder> {

    private final List<NodeModel> nodeModelArrayList = new ArrayList<>();

    public NodeAdapter() {
    }

    @NonNull
    @Override
    public NodeAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.node_card_fragment, parent, false);
        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NodeAdapter.Viewholder holder, int position) {
        NodeModel model = nodeModelArrayList.get(position);
        holder.nodeName.setText(model.nodeName);
        if (model.sensors != null && model.sensors.size() > 2) {
            holder.sensor1.setText(model.sensors.get(0).sensorType);
            holder.sensor2.setText(model.sensors.get(1).sensorType);
        } else {
            holder.sensor1.setText("");
            holder.sensor2.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return nodeModelArrayList.size();
    }

    public void addAllNodes(List<NodeModel> nodeModels) {
        this.nodeModelArrayList.addAll(nodeModels);
        notifyDataSetChanged();
    }

    public void addNode(NodeModel nodeModel) {
        nodeModelArrayList.add(nodeModel);
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView nodeName, sensor1, sensor2;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nodeName = itemView.findViewById(R.id.nodeName);
            sensor1 = itemView.findViewById(R.id.sensor1);
            sensor2 = itemView.findViewById(R.id.sensor2);
        }
    }
}
