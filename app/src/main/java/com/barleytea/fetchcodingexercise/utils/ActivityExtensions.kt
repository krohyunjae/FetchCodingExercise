package com.barleytea.fetchcodingexercise.utils

import android.app.Activity
import android.content.pm.ActivityInfo

fun Activity.setOrientation() {
    requestedOrientation = if (resources.configuration.smallestScreenWidthDp >= 600) {
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    } else {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}