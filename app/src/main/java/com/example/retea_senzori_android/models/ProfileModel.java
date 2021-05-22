package com.example.retea_senzori_android.models;

import com.example.retea_senzori_android.persistance.FirebaseDocument;

import java.util.List;

public class ProfileModel extends FirebaseDocument {

    public String username;
    public String email;
    public List<NodeModel> nodes;
}
