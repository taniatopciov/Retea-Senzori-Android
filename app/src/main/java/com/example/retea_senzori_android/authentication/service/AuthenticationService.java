package com.example.retea_senzori_android.authentication.service;

import com.example.retea_senzori_android.models.ProfileModel;
import com.example.retea_senzori_android.observables.Subject;

public interface AuthenticationService {
    Subject<String> login(String email, String password);

    void logout();

    boolean isLoggedIn();

    Subject<ProfileModel> getLoggedUserData();

    Subject<String> register(String email, String password, String username);
}
