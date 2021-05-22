package com.example.retea_senzori_android.persistance;

import com.example.retea_senzori_android.observables.Subject;

import java.util.List;

public interface FirebaseRepository<T extends FirebaseDocument> {
    Subject<T> getDocument(String pathToDocument, Class<T> tClass);

    Subject<List<T>> getAllDocuments(String pathToCollection, Class<T> tClass);

    Subject<String> createDocument(String pathToCollection, T document);

    Subject<Boolean> setDocument(String pathToCollection, String documentId, T document);

    Subject<Boolean> deleteDocument(String pathToCollection, String documentId);

    Subject<Boolean> updateDocument(String pathToDocument, T document);
}
