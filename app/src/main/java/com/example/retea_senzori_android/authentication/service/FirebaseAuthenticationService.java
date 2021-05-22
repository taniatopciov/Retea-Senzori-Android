package com.example.retea_senzori_android.authentication.service;

import com.example.retea_senzori_android.models.ProfileModel;
import com.example.retea_senzori_android.observables.Subject;
import com.example.retea_senzori_android.persistance.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class FirebaseAuthenticationService implements AuthenticationService {
    private static final String USER_DATA_COLLECTION_PATH = "users";

    private final FirebaseAuth firebaseAuth;

    private final FirebaseRepository<ProfileModel> profileModelFirebaseRepository;

    private Subject<ProfileModel> profileModelSubject;

    public FirebaseAuthenticationService(FirebaseRepository<ProfileModel> profileModelFirebaseRepository) {
        this.profileModelFirebaseRepository = profileModelFirebaseRepository;
        firebaseAuth = FirebaseAuth.getInstance();
        profileModelSubject = new Subject<>();
        firebaseAuth.addAuthStateListener(firebaseAuth1 -> {
            FirebaseUser currentUser = firebaseAuth1.getCurrentUser();
            if (currentUser != null) {
                String path = USER_DATA_COLLECTION_PATH + "/" + currentUser.getUid();

                profileModelFirebaseRepository.getDocument(path, ProfileModel.class).subscribe(profileModel -> {
                    profileModelSubject.setState(profileModel);
                });
            } else {
                profileModelSubject.setState(null);
            }
        });
    }

    @Override
    public Subject<String> login(String email, String password) {
        Subject<String> subject = new Subject<>();

        if (email.isEmpty() || password.isEmpty()) {
            subject.setState("Some Fields Are Empty");
            return subject;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subject.setState(null);
            } else {
                subject.setState(task.getException().getMessage());
            }
        });
        return subject;
    }

    @Override
    public void logout() {
        profileModelSubject.setState(null);
        firebaseAuth.signOut();
    }

    @Override
    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public Subject<ProfileModel> getLoggedUserData() {
        return profileModelSubject;
    }

    @Override
    public Subject<String> register(String email, String password, String username) {

        Subject<String> subject = new Subject<>();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            subject.setState("Some Fields Are Empty");
            return subject;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                subject.setState(task.getException().getMessage());
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

                        profileModelSubject.setState(profile);

                        profileModelFirebaseRepository.setDocument(USER_DATA_COLLECTION_PATH, currentUser.getUid(), profile).subscribe(created -> {
                            if (!created) {
                                subject.setState("Creation failed");
                            } else {
                                subject.setState(null);
                            }
                        });
                    }

                    return null;
                });
        return subject;
    }
}
