package com.jarvis.ai.action.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jarvis.ai.action.ReminderScheduler
import com.jarvis.ai.data.repository.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var reminderScheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val now = System.currentTimeMillis()
                reminderRepository.pending(now).forEach { reminderScheduler.schedule(it) }
            } finally { pending.finish() }
        }
    }
}