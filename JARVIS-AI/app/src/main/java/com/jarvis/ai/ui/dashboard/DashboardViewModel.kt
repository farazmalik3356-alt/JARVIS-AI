package com.jarvis.ai.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.ai.data.local.db.entity.ReminderEntity
import com.jarvis.ai.data.local.secure.SecureKeyStore
import com.jarvis.ai.data.repository.ReminderRepository
import com.jarvis.ai.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DashboardUiState(
    val hasApiKey: Boolean = false,
    val reminders: List<ReminderEntity> = emptyList()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val secureKeyStore: SecureKeyStore,
    private val reminderRepository: ReminderRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    val hasApiKey: StateFlow<Boolean> = kotlinx.coroutines.flow.flow {
        emit(secureKeyStore.hasApiKey())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), secureKeyStore.hasApiKey())

    val reminders: StateFlow<List<ReminderEntity>> = reminderRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}