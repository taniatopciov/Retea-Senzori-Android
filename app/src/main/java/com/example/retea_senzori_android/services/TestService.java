package com.example.retea_senzori_android.services;

import com.example.retea_senzori_android.observables.Subject;

import java.util.List;

public interface TestService {
    Subject<TestClass> getTestClass(String id);

    Subject<List<TestClass>> getAllTestClasses();

    Subject<String> createTestClass(String value);
}
