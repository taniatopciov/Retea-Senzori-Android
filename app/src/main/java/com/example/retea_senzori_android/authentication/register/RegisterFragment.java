package com.example.retea_senzori_android.authentication.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.authentication.service.AuthenticationService;
import com.example.retea_senzori_android.databinding.FragmentRegisterBinding;
import com.example.retea_senzori_android.di.Injectable;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class RegisterFragment extends Fragment {

    @Injectable
    AuthenticationService authenticationService;

    private FragmentRegisterBinding binding;

    public RegisterFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.goToLogin.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(RegisterFragmentDirections.navigateToLogin());
        });

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        binding.registerButton.setOnClickListener(view -> {
            String email = binding.emailRegister.getText().toString();
            String password = binding.passwordRegister.getText().toString();
            String confirmPassword = binding.passwordConfirmRegister.getText().toString();
            String username = binding.usernameRegister.getText().toString();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
                Snackbar.make(view, "Some Fields Are Empty", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Snackbar.make(view, "Passwords do not match", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (!validateEmail(email)) {
                Snackbar.make(view, "Invalid Email Address", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            boolean[] called = {false};

            authenticationService.register(email, password, username).subscribe(errorMessage -> {
                if (errorMessage == null) {
                    if (!called[0]) {
                        called[0] = true;
                        navController.navigate(RegisterFragmentDirections.navigateToHomePage());
                    }
                } else {
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        });

        return binding.getRoot();
    }

    private boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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