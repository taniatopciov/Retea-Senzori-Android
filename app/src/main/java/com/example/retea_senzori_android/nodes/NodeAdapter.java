package com.example.retea_senzori_android.nodes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.models.NodeModel;

import java.util.ArrayList;
import java.util.List;

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
        holder.viewDetailsButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(HomePageNodesFragmentDirections.actionNodesViewToNodeDetailsFragment(model));
        });
        if (model.sensors != null && model.sensors.size() >= 2) {
            holder.sensor1.setText(model.sensors.get(0).sensorType.toString());
            holder.sensor2.setText(model.sensors.get(1).sensorType.toString());
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

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView nodeName;
        private final TextView sensor1;
        private final TextView sensor2;
        private final Button viewDetailsButton;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nodeName = itemView.findViewById(R.id.nodeName);
            sensor1 = itemView.findViewById(R.id.sensor1);
            sensor2 = itemView.findViewById(R.id.sensor2);
            viewDetailsButton = itemView.findViewById(R.id.detailsButton);
        }
    }
}
