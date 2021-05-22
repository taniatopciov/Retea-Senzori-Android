package com.example.retea_senzori_android.authentication.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.retea_senzori_android.databinding.FragmentRegisterBinding;
import com.google.android.material.snackbar.Snackbar;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.goToLogin.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(RegisterFragmentDirections.navigateToLogin());
        });

//        binding.registerButton.setOnClickListener(view -> {
//            String email = binding.editTextEmailAddressRegister.getText().toString();
//            String password = binding.editTextPasswordRegister.getText().toString();
//            String username = binding.editTextNameRegister.getText().toString();
//
//            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || userType.isEmpty()) {
//                Snackbar.make(view, "Some Fields Are Empty", Snackbar.LENGTH_SHORT)
//                        .show();
//                return;
//            }
//
//            if (!validateEmail(email)) {
//                Snackbar.make(view, "Invalid Email Address", Snackbar.LENGTH_SHORT)
//                        .show();
//                return;
//            }
//
//            authenticationService.register(email, password, name, userType, errorMessage -> {
//                if (errorMessage == null) {
//                    Intent intent = new Intent();
//                    intent.putExtra(AUTH_ACTIVITY_REQUEST_EXTRA, "register");
//                    getActivity().setResult(Activity.RESULT_OK, intent);
//                    getActivity().finish();
//                } else {
//                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
//                            .show();
//                }
//            });
//        });

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