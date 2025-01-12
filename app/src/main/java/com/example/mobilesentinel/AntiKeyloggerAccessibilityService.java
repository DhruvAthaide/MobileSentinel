package com.example.mobilesentinel;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class AntiKeyloggerAccessibilityService extends AccessibilityService {

    private static final String TAG = "AntiKeyloggerService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            String sourcePackage = event.getPackageName().toString();
            Log.d(TAG, "Text change detected from package: " + sourcePackage);

            if (isSuspiciousApp(sourcePackage)) {
                warnUser(sourcePackage);
            }
        }
    }

    @Override
    public void onInterrupt() {
        // Handle interruptions
    }

    private boolean isSuspiciousApp(String packageName) {
        return packageName.toLowerCase().contains("keylogger") ||
                packageName.toLowerCase().contains("keyboard");
    }

    private void warnUser(String packageName) {
        String warningMessage = "Warning: Potential keylogger detected: " + packageName;
        Toast.makeText(this, warningMessage, Toast.LENGTH_LONG).show();
        Log.w(TAG, warningMessage);
    }
}