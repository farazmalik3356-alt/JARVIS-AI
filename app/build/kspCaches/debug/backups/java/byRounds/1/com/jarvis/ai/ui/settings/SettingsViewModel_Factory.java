package com.jarvis.ai.ui.settings;

import com.jarvis.ai.data.local.secure.SecureKeyStore;
import com.jarvis.ai.data.repository.ConversationRepository;
import com.jarvis.ai.data.repository.GeminiRepository;
import com.jarvis.ai.data.repository.ReminderRepository;
import com.jarvis.ai.data.repository.SettingsRepository;
import com.jarvis.ai.voice.VoiceCoordinator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SecureKeyStore> secureKeyStoreProvider;

  private final Provider<GeminiRepository> geminiRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<ConversationRepository> conversationRepositoryProvider;

  private final Provider<ReminderRepository> reminderRepositoryProvider;

  private final Provider<VoiceCoordinator> voiceCoordinatorProvider;

  public SettingsViewModel_Factory(Provider<SecureKeyStore> secureKeyStoreProvider,
      Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<ReminderRepository> reminderRepositoryProvider,
      Provider<VoiceCoordinator> voiceCoordinatorProvider) {
    this.secureKeyStoreProvider = secureKeyStoreProvider;
    this.geminiRepositoryProvider = geminiRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.conversationRepositoryProvider = conversationRepositoryProvider;
    this.reminderRepositoryProvider = reminderRepositoryProvider;
    this.voiceCoordinatorProvider = voiceCoordinatorProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(secureKeyStoreProvider.get(), geminiRepositoryProvider.get(), settingsRepositoryProvider.get(), conversationRepositoryProvider.get(), reminderRepositoryProvider.get(), voiceCoordinatorProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<SecureKeyStore> secureKeyStoreProvider,
      Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<ReminderRepository> reminderRepositoryProvider,
      Provider<VoiceCoordinator> voiceCoordinatorProvider) {
    return new SettingsViewModel_Factory(secureKeyStoreProvider, geminiRepositoryProvider, settingsRepositoryProvider, conversationRepositoryProvider, reminderRepositoryProvider, voiceCoordinatorProvider);
  }

  public static SettingsViewModel newInstance(SecureKeyStore secureKeyStore,
      GeminiRepository geminiRepository, SettingsRepository settingsRepository,
      ConversationRepository conversationRepository, ReminderRepository reminderRepository,
      VoiceCoordinator voiceCoordinator) {
    return new SettingsViewModel(secureKeyStore, geminiRepository, settingsRepository, conversationRepository, reminderRepository, voiceCoordinator);
  }
}
