package com.example.retea_senzori_android.authentication.service;

import androidx.lifecycle.Observer;

import com.example.retea_senzori_android.profile.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


public class FirebaseAuthenticationService implements AuthenticationService {
    private static final String USER_DATA_COLLECTION_PATH = "users";

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    private LoggedUserData loggedUserData;

    public FirebaseAuthenticationService() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void login(String email, String password, Observer<String> observer) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void register(String email, String password, String username, Observer<String> observer) {

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            observer.onChanged("Some Fields Are Empty");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                observer.onChanged(task.getException().getMessage());
            }
        })
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();

                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        return currentUser.updateProfile(profileChangeRequest);
                    }
                    return null;
                })
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                        ProfileModel profile = new ProfileModel();

                        profile.id = currentUser.getUid();
                        profile.email = currentUser.getEmail();
                        profile.username = username;

                        setLoggedUserData(profile);

                        String path = USER_DATA_COLLECTION_PATH + "/" + currentUser.getUid();
                        firestore.document(path).set(profile)
                                .addOnSuccessListener(documentReference -> observer.onChanged(null))
                                .addOnFailureListener(e -> observer.onChanged("Creation failed"));
                    }

                    return null;
                });
    }

    private void setLoggedUserData(ProfileModel profile) {
        if (loggedUserData == null) {
            loggedUserData = new LoggedUserData();
        }
        loggedUserData.id = profile.id;
        loggedUserData.email = profile.email;
        loggedUserData.name = profile.username;
    }
}
