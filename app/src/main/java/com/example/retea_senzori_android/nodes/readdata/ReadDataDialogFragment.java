package com.example.retea_senzori_android.nodes.readdata;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.observables.Subject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ReadDataDialogFragment extends DialogFragment {

    private Subject<Void> dataReadSubject;

    public static ReadDataDialogFragment newInstance(Subject<Void> dataReadSubject) {
        ReadDataDialogFragment readDataDialogFragment = new ReadDataDialogFragment();
        readDataDialogFragment.dataReadSubject = dataReadSubject;

        return readDataDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_read_data_dialog, null);

        dataReadSubject.subscribe(unused -> dismiss());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}