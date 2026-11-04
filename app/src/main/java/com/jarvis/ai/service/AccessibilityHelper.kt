package com.jarvis.ai.service

import android.content.Context
import android.content.Intent
import android.provider.Settings

object AccessibilityHelper {
    fun isServiceEnabled(context: Context): Boolean {
        val service = "${context.packageName}/${JarvisAccessibilityService::class.java.name}"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.contains(service)
    }

    fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
