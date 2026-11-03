package com.jarvis.ai.action

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jarvis.ai.action.receiver.ReminderReceiver
import com.jarvis.ai.data.local.db.entity.ReminderEntity
import com.jarvis.ai.data.repository.ReminderRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reminderRepository: ReminderRepository
) {
    suspend fun create(
        title: String,
        isoTime: String,
        notes: String?
    ): Result<ReminderEntity> {
        val whenMs = runCatching {
            OffsetDateTime.parse(isoTime).toInstant().toEpochMilli()
        }.getOrElse {
            return Result.failure(IllegalArgumentException("Invalid reminder time"))
        }
        if (whenMs <= System.currentTimeMillis() + 1000L) {
            return Result.failure(IllegalArgumentException("Reminder time must be in the future"))
        }
        val entity = ReminderEntity(
            id = UUID.randomUUID().toString(),
            title = title.trim(),
            description = notes,
            triggerAtMillis = whenMs,
            createdAt = System.currentTimeMillis()
        )
        reminderRepository.upsert(entity)
        schedule(entity)
        return Result.success(entity)
    }

    fun schedule(entity: ReminderEntity) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = buildPendingIntent(entity)
        val canExact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            am.canScheduleExactAlarms() else true
        try {
            if (canExact) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, entity.triggerAtMillis, pi)
            } else {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, entity.triggerAtMillis, pi)
            }
        } catch (e: SecurityException) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, entity.triggerAtMillis, pi)
        }
    }

    fun cancel(entity: ReminderEntity) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(buildPendingIntent(entity))
    }

    private fun buildPendingIntent(entity: ReminderEntity): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION_FIRE_REMINDER
            putExtra(EXTRA_ID, entity.id)
            putExtra(EXTRA_TITLE, entity.title)
            putExtra(EXTRA_NOTES, entity.description)
        }
        return PendingIntent.getBroadcast(
            context,
            entity.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val ACTION_FIRE_REMINDER = "com.jarvis.ai.action.FIRE_REMINDER"
        const val EXTRA_ID = "id"
        const val EXTRA_TITLE = "title"
        const val EXTRA_NOTES = "notes"
    }
}