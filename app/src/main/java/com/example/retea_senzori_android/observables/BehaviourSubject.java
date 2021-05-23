package com.example.retea_senzori_android.observables;

public class BehaviourSubject<T> extends Subject<T>{
    private T state;

    @Override
    public void subscribe(Observer<T> observer) {
        super.subscribe(observer);
        observer.observe(state);
    }

    @Override
    public void setState(T state) {
        this.state = state;
        super.setState(state);
    }
}
