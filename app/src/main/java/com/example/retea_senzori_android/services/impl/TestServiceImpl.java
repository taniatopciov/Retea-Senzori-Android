package com.example.retea_senzori_android.services.impl;

import com.example.retea_senzori_android.observables.Subject;
import com.example.retea_senzori_android.persistance.FirebaseRepository;
import com.example.retea_senzori_android.services.TestClass;
import com.example.retea_senzori_android.services.TestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestServiceImpl implements TestService {

    private final String TEST_COLLECTION_PATH = "test";

    private final FirebaseRepository<TestClass> testClassFirebaseRepository;

    public TestServiceImpl(FirebaseRepository<TestClass> testClassFirebaseRepository) {
        this.testClassFirebaseRepository = testClassFirebaseRepository;
    }

    @Override
    public Subject<TestClass> getTestClass(String id) {
        return testClassFirebaseRepository.getDocument(TEST_COLLECTION_PATH + "/" + id, TestClass.class);
    }

    @Override
    public Subject<List<TestClass>> getAllTestClasses() {
        return testClassFirebaseRepository.getAllDocuments(TEST_COLLECTION_PATH, TestClass.class);
    }

    @Override
    public Subject<String> createTestClass(String value) {
        TestClass testClass = new TestClass();
        testClass.value = value;

        return testClassFirebaseRepository.createDocument(TEST_COLLECTION_PATH, testClass);
    }

    @Override
    public Subject<Boolean> deleteTestClass(String id) {
        return testClassFirebaseRepository.deleteDocument(TEST_COLLECTION_PATH, id);
    }

    @Override
    public Subject<Boolean> updateTestClass(String id, String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("value", value);
        return testClassFirebaseRepository.updateDocument(TEST_COLLECTION_PATH + "/" + id, map);
    }
}
