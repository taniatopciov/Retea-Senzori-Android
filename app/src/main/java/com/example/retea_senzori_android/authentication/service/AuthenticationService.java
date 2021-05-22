package com.example.retea_senzori_android.authentication.service;

import androidx.lifecycle.Observer;

public interface AuthenticationService {

    void login(String email, String password, Observer<String> observer);

    void logout();

    void register(String email, String password, String name, Observer<String> observer);
}
