package com.example.j2demo;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

public class CustomDensity {
    private static float nonCompatDensity = 0f;
    private static float nonCompatScaleDensity = 0f;
    private final static float designWidthDp = 375;

    public static void setCustomDensity(
            @NonNull Activity activity,
            @NonNull final Application application
    ) {
        final DisplayMetrics appDm = application.getResources().getDisplayMetrics();
        if (nonCompatDensity == 0) {
            nonCompatDensity = appDm.density;
            nonCompatScaleDensity = appDm.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration configuration) {
                    if (configuration.fontScale > 0) {
                        nonCompatScaleDensity = application
                                .getResources()
                                .getDisplayMetrics()
                                .scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        final float targetDensity = appDm.widthPixels / designWidthDp;
        final float targetScaledDensity = targetDensity * (nonCompatScaleDensity / nonCompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDm.density = targetDensity;
        appDm.scaledDensity = targetScaledDensity;
        appDm.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();
        activityDm.density = targetDensity;
        activityDm.scaledDensity = targetScaledDensity;
        activityDm.densityDpi = targetDensityDpi;
    }
}
