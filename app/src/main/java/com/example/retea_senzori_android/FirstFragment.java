package com.example.retea_senzori_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.databinding.FragmentFirstBinding;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    FragmentFirstBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);

        binding.buttonFirst.setOnClickListener(view -> {
            NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_bluetoothManagerFragment);
        });

        return binding.getRoot();
    }
}