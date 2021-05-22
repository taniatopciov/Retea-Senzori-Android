package com.example.retea_senzori_android.nodes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retea_senzori_android.R;

import java.util.ArrayList;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.Viewholder> {

    private Context context;
    private ArrayList<NodeModel> nodeModelArrayList;

    public NodeAdapter(Context context, ArrayList<NodeModel> nodeModelArrayList) {
        this.context = context;
        this.nodeModelArrayList = nodeModelArrayList;
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
        holder.nodeName.setText(model.node_name);
        //holder.sensor1.setText(model.sensor1_name);
        //holder.sensor2.setText(model.sensor2_name);
    }

    @Override
    public int getItemCount() {
        return nodeModelArrayList.size();
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
