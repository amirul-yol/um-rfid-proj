package com.example.rfidinventory.utils;

import android.os.Build;

public class DeviceUtils {
    private static final String C66_MODEL = "C66"; // Update this if needed with actual model name

    public static boolean isC66Device() {
        return Build.MODEL.contains(C66_MODEL);
    }
}
