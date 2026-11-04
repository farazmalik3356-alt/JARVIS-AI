package com.jarvis.ai.chat;

import com.jarvis.ai.data.repository.ConversationRepository;
import com.jarvis.ai.data.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ChatSessionManager_Factory implements Factory<ChatSessionManager> {
  private final Provider<ConversationRepository> conversationRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public ChatSessionManager_Factory(Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.conversationRepositoryProvider = conversationRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public ChatSessionManager get() {
    return newInstance(conversationRepositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static ChatSessionManager_Factory create(
      Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new ChatSessionManager_Factory(conversationRepositoryProvider, settingsRepositoryProvider);
  }

  public static ChatSessionManager newInstance(ConversationRepository conversationRepository,
      SettingsRepository settingsRepository) {
    return new ChatSessionManager(conversationRepository, settingsRepository);
  }
}
