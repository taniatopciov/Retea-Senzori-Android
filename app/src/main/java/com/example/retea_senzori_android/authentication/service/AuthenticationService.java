package com.example.retea_senzori_android.authentication.service;

import androidx.lifecycle.Observer;

import java.util.function.Consumer;

public interface AuthenticationService {

    void login(String email, String password, Observer<String> observer);

    void logout();

    void register(String email, String password, String name, Observer<String> observer);

    public boolean isLoggedIn();

    public void getLoggedUserData(Consumer<LoggedUserData> userDataConsumer);
}
