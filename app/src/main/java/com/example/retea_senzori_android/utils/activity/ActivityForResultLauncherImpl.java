package com.example.retea_senzori_android.utils.activity;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCaller;

public class ActivityForResultLauncherImpl implements ActivityForResultLauncher {

    private final BetterActivityResult<Intent, ActivityResult> activityLauncher;

    public ActivityForResultLauncherImpl(ActivityResultCaller caller) {
        activityLauncher = BetterActivityResult.registerActivityForResult(caller);
    }

    @Override
    public void launch(Intent intent, BetterActivityResult.OnActivityResult<ActivityResult> onActivityResult) {
        activityLauncher.launch(intent, onActivityResult);
    }
}
