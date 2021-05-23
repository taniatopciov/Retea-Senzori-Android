package com.example.retea_senzori_android.utils.runners;

import java.util.Timer;
import java.util.TimerTask;

public class CyclicRunner {

    private final Timer timer;
    private final int millisInterval;

    public CyclicRunner(int millisInterval) {
        this.millisInterval = millisInterval;
        timer = new Timer();
    }

    public void run(Runnable runnable) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, 0, millisInterval);
    }

    public void stop() {
        timer.cancel();
        timer.purge();
    }
}
