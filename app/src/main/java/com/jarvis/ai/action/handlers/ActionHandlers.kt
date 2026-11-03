package com.jarvis.ai.action.handlers

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.jarvis.ai.action.StepOutcome
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLauncher @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun open(appName: String): StepOutcome {
        val pm = context.packageManager
        // 1) Try direct launch intent by label match among installed packages
        val matches = mutableListOf<Pair<String, String>>() // pkg -> label
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val activities = pm.queryIntentActivities(launcherIntent, 0)
        for (ri in activities) {
            val label = ri.loadLabel(pm).toString()
            val pkg = ri.activityInfo.packageName
            matches += pkg to label
        }
        val norm = appName.trim().lowercase()
        val exact = matches.firstOrNull { it.second.lowercase() == norm }
        val partial = exact ?: matches.firstOrNull { it.second.lowercase().contains(norm) }
        val chosenPkg = partial?.first
        if (chosenPkg == null) {
            return StepOutcome.Failure("App \"$appName\" is not installed or not visible.")
        }
        val launch = pm.getLaunchIntentForPackage(chosenPkg)
            ?: return StepOutcome.Failure("App \"$appName\" has no launch activity.")
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            context.startActivity(launch)
            StepOutcome.Success("Opened ${partial.second}")
        } catch (e: ActivityNotFoundException) {
            StepOutcome.Failure("Cannot launch ${partial.second}.")
        } catch (e: SecurityException) {
            StepOutcome.Failure("Launch blocked by system.")
        }
    }
}

@Singleton
class SettingsLauncher @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun open(target: String): StepOutcome {
        val action = when (target) {
            "wifi" -> Settings.ACTION_WIFI_SETTINGS
            "bluetooth" -> Settings.ACTION_BLUETOOTH_SETTINGS
            "location" -> Settings.ACTION_LOCATION_SOURCE_SETTINGS
            "display" -> Settings.ACTION_DISPLAY_SETTINGS
            "sound" -> Settings.ACTION_SOUND_SETTINGS
            "apps" -> Settings.ACTION_APPLICATION_SETTINGS
            "battery" -> "android.settings.BATTERY_SAVER_SETTINGS"
            "date_time" -> Settings.ACTION_DATE_SETTINGS
            else -> Settings.ACTION_SETTINGS
        }
        val intent = Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            context.startActivity(intent)
            StepOutcome.Success("Opened settings: $target")
        } catch (e: Exception) {
            StepOutcome.Failure("This device doesn't expose that settings screen.")
        }
    }
}

@Singleton
class UrlOpener @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun open(url: String): StepOutcome {
        val uri = Uri.parse(url)
        val scheme = uri.scheme?.lowercase()
        if (scheme != "http" && scheme != "https") {
            return StepOutcome.Failure("Only http(s) URLs are allowed.")
        }
        val intent = Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            context.startActivity(intent)
            StepOutcome.Success("Opened $url")
        } catch (e: ActivityNotFoundException) {
            StepOutcome.Failure("No app can handle that link.")
        }
    }
}

@Singleton
class ShareIntentHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun share(text: String): StepOutcome {
        if (text.isBlank()) return StepOutcome.Failure("Nothing to share.")
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(send, "Share via").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(chooser)
            StepOutcome.Success("Shared.")
        } catch (e: ActivityNotFoundException) {
            StepOutcome.Failure("No share target available.")
        }
    }
}

@Singleton
class WebSearchHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun search(query: String): StepOutcome {
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, query)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(intent)
            StepOutcome.Success("Searching the web.")
        } catch (e: Exception) {
            StepOutcome.Failure("No browser available.")
        }
    }
}