package com.example.retea_senzori_android.persistance.impl;

import com.example.retea_senzori_android.observables.Subject;
import com.example.retea_senzori_android.persistance.FirebaseDocument;
import com.example.retea_senzori_android.persistance.FirebaseRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepositoryImpl<T extends FirebaseDocument> implements FirebaseRepository<T> {

    private final FirebaseFirestore firestore;

    public FirebaseRepositoryImpl() {
        firestore = FirebaseFirestore.getInstance();
    }


    @Override
    public Subject<T> getDocument(String pathToDocument, Class<T> tClass) {
        Subject<T> subject = new Subject<>();

        firestore.document(pathToDocument).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                T modelDTO = documentSnapshot.toObject(tClass);
                if (modelDTO != null) {
                    modelDTO.id = documentSnapshot.getId();
                }

                subject.setState(modelDTO);
            } else {
                subject.setState(null);
            }
        }).addOnFailureListener(e -> subject.setState(null));

        return subject;
    }

    @Override
    public Subject<List<T>> getAllDocuments(String pathToCollection, Class<T> tClass) {

        Subject<List<T>> subject = new Subject<>();

        firestore.collection(pathToCollection).get().addOnCompleteListener(task -> {
            QuerySnapshot result = task.getResult();
            List<T> documents = new ArrayList<>();
            if (task.isSuccessful() && result != null) {
                for (QueryDocumentSnapshot documentSnapshot : result) {
                    T modelDTO = documentSnapshot.toObject(tClass);
                    modelDTO.id = documentSnapshot.getId();
                    documents.add(modelDTO);
                }
            }

            subject.setState(documents);
        });


        return subject;
    }

    @Override
    public Subject<String> createDocument(String pathToCollection, T document) {

        Subject<String> subject = new Subject<>();

        firestore.collection(pathToCollection).add(document)
                .addOnSuccessListener(documentReference -> subject.setState(documentReference.getId()))
                .addOnFailureListener(e -> subject.setState(null));

        return subject;
    }

    @Override
    public Subject<Boolean> deleteDocument(String pathToCollection, String documentId) {
        return null;
    }

    @Override
    public Subject<Boolean> updateDocument(String pathToDocument, T document) {
        return null;
    }
}
