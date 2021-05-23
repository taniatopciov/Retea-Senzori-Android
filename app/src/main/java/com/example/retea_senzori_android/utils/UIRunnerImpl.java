package com.example.retea_senzori_android.utils;

import android.app.Activity;

public class UIRunnerImpl implements UIRunner {
    private final Activity activity;

    public UIRunnerImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void run(Runnable runnable) {
        activity.runOnUiThread(runnable);
    }
}
