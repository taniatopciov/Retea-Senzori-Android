package com.example.retea_senzori_android.authentication.login;

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

import com.example.retea_senzori_android.authentication.service.AuthenticationService;
import com.example.retea_senzori_android.databinding.LoginFragmentBinding;
import com.example.retea_senzori_android.di.Injectable;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;


public class LoginFragment extends Fragment {

    @Injectable
    AuthenticationService authenticationService;

    private LoginFragmentBinding binding;

    public LoginFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = LoginFragmentBinding.inflate(inflater, container, false);

        binding.loginButton.setOnClickListener(view -> {
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(view, "Some Fields Are Empty", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            authenticationService.login(email, password).subscribe(errorMessage -> {
                if (errorMessage == null) {
                    Navigation.findNavController(view).navigate(LoginFragmentDirections.navigateToHomePage());
                } else {
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        });

        binding.goToRegister.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(LoginFragmentDirections.navigateToRegister());
        });
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