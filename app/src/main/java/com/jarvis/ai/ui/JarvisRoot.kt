package com.jarvis.ai.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.jarvis.ai.data.local.prefs.UserPreferences
import com.jarvis.ai.data.repository.SettingsRepository
import com.jarvis.ai.ui.chat.ChatScreen
import com.jarvis.ai.ui.dashboard.DashboardScreen
import com.jarvis.ai.ui.history.HistoryScreen
import com.jarvis.ai.ui.onboarding.ApiSetupScreen
import com.jarvis.ai.ui.onboarding.OnboardingScreen
import com.jarvis.ai.ui.settings.SettingsScreen
import com.jarvis.ai.ui.voice.VoiceScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = settingsRepository.preferences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    fun markOnboardingComplete() = viewModelScope.launch {
        settingsRepository.setOnboardingComplete(true)
    }
}

private enum class Root { Onboarding, ApiSetup, Dashboard, Chat, Voice, History, Settings }

@Composable
fun JarvisRoot(viewModel: RootViewModel = hiltViewModel()) {
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()

    Crossfade(
        targetState = prefs.onboardingComplete,
        animationSpec = tween(220),
        label = "root"
    ) { onboarded ->
        if (!onboarded) {
            RootFlow(viewModel)
        } else {
            MainFlow(prefs)
        }
    }
}

@Composable
private fun RootFlow(viewModel: RootViewModel) {
    var showApi by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(false)
    }
    if (!showApi) {
        OnboardingScreen(
            onContinue = { showApi = true },
            onSkip = { viewModel.markOnboardingComplete() }
        )
    } else {
        ApiSetupScreen(
            onDone = { viewModel.markOnboardingComplete() }
        )
    }
}

@Composable
private fun MainFlow(prefs: UserPreferences) {
    var route by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(Root.Dashboard)
    }

    // Android Back Button ko handle karne ke liye
    BackHandler(enabled = route != Root.Dashboard) {
        route = Root.Dashboard
    }

    // Screen cut hone se bachane ke liye insets padding
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        when (route) {
            Root.Dashboard -> DashboardScreen(
                onOpenChat = { route = Root.Chat },
                onOpenVoice = { route = Root.Voice },
                onOpenHistory = { route = Root.History },
                onOpenSettings = { route = Root.Settings }
            )
            Root.Chat -> ChatScreen(
                onBack = { route = Root.Dashboard },
                voiceEnabled = prefs.voiceOutputEnabled
            )
            Root.Voice -> VoiceScreen(
                onBack = { route = Root.Dashboard },
                speechLanguage = prefs.speechLanguage,
                speechRate = prefs.speechRate
            )
            Root.History -> HistoryScreen(onBack = { route = Root.Dashboard })
            Root.Settings -> SettingsScreen(onBack = { route = Root.Dashboard })
            Root.Onboarding, Root.ApiSetup -> Unit
        }
    }
}
