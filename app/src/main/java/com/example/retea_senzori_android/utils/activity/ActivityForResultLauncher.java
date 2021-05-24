package com.example.retea_senzori_android.utils.activity;

import android.content.Intent;

import androidx.activity.result.ActivityResult;

public interface ActivityForResultLauncher{
    void launch(Intent intent, BetterActivityResult.OnActivityResult<ActivityResult> onActivityResult);
}
