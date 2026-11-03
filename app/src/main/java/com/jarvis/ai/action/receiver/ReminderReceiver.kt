package com.jarvis.ai.action.receiver

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.jarvis.ai.MainActivity
import com.jarvis.ai.R
import com.jarvis.ai.action.ReminderScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderRepository: com.jarvis.ai.data.repository.ReminderRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ReminderScheduler.ACTION_FIRE_REMINDER) return
        val id = intent.getStringExtra(ReminderScheduler.EXTRA_ID) ?: return
        val title = intent.getStringExtra(ReminderScheduler.EXTRA_TITLE) ?: "Reminder"
        val notes = intent.getStringExtra(ReminderScheduler.EXTRA_NOTES)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Cannot notify; still mark as fired to avoid loops
        } else {
            postNotification(context, id, title, notes)
        }

        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try { reminderRepository.markFired(id) } finally { pending.finish() }
        }
    }

    private fun postNotification(context: Context, id: String, title: String, notes: String?) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Reminders", NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "JARVIS reminders" }
            nm.createNotificationChannel(channel)
        }
        val tap = PendingIntent.getActivity(
            context, id.hashCode(),
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .apply { notes?.let { setContentText(it) } }
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(tap)
            .build()
        nm.notify(id.hashCode(), notif)
    }

    companion object {
        private const val CHANNEL_ID = "jarvis_reminders"
    }
}