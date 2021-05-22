package com.example.retea_senzori_android.observables;

import java.util.ArrayList;
import java.util.List;

public class Subject<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    public void subscribe(Observer<T> observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer<T> observer) {
        observers.remove(observer);
    }

    public void setState(T state) {
        notifyObservers(state);
    }

    protected void notifyObservers(T state) {
        for (Observer<T> observer : observers) {
            observer.observe(state);
        }
    }
}
