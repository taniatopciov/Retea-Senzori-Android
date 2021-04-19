package com.example.retea_senzori_android.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.databinding.FragmentRegisterBinding;
import com.example.retea_senzori_android.databinding.LoginFragmentBinding;
import com.example.retea_senzori_android.ui.register.RegisterFragment;

import java.util.zip.Inflater;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}