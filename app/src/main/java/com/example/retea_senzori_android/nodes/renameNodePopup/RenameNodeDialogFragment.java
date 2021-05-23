package com.example.retea_senzori_android.nodes.renameNodePopup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.di.ServiceLocator;

import java.util.function.Consumer;

public class RenameNodeDialogFragment extends DialogFragment {
    private Consumer<String> onRenameButtonClickConsumer;
    private String nodeName;

    public RenameNodeDialogFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static RenameNodeDialogFragment newInstance(String nodeName, Consumer<String> onRenameButtonClick) {
        RenameNodeDialogFragment renameNodeDialogFragment = new RenameNodeDialogFragment();
        renameNodeDialogFragment.onRenameButtonClickConsumer = onRenameButtonClick;
        renameNodeDialogFragment.nodeName = nodeName;

        return renameNodeDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rename_node_dialog, null);

        TextView renameNodeTextView = view.findViewById(R.id.renameNodeTextView);
        renameNodeTextView.setText(nodeName);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Rename Node")
                .setView(view)
                .setPositiveButton("Rename", (dialogInterface, i) -> {
                    onRenameButtonClickConsumer.accept(renameNodeTextView.getText().toString());
                    dismiss();
                })
                .setNegativeButton("Close", (dialogInterface, i) -> dismiss())
                .create();
    }
}
