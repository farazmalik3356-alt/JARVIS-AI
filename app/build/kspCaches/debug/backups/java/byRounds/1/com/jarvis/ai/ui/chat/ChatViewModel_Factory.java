package com.jarvis.ai.ui.chat;

import com.jarvis.ai.chat.ChatOrchestrator;
import com.jarvis.ai.chat.ChatSessionManager;
import com.jarvis.ai.data.repository.ConversationRepository;
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<ChatSessionManager> sessionManagerProvider;

  private final Provider<ChatOrchestrator> orchestratorProvider;

  private final Provider<ConversationRepository> conversationRepositoryProvider;

  private final Provider<VoiceCoordinator> voiceCoordinatorProvider;

  public ChatViewModel_Factory(Provider<ChatSessionManager> sessionManagerProvider,
      Provider<ChatOrchestrator> orchestratorProvider,
      Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<VoiceCoordinator> voiceCoordinatorProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
    this.orchestratorProvider = orchestratorProvider;
    this.conversationRepositoryProvider = conversationRepositoryProvider;
    this.voiceCoordinatorProvider = voiceCoordinatorProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(sessionManagerProvider.get(), orchestratorProvider.get(), conversationRepositoryProvider.get(), voiceCoordinatorProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<ChatSessionManager> sessionManagerProvider,
      Provider<ChatOrchestrator> orchestratorProvider,
      Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<VoiceCoordinator> voiceCoordinatorProvider) {
    return new ChatViewModel_Factory(sessionManagerProvider, orchestratorProvider, conversationRepositoryProvider, voiceCoordinatorProvider);
  }

  public static ChatViewModel newInstance(ChatSessionManager sessionManager,
      ChatOrchestrator orchestrator, ConversationRepository conversationRepository,
      VoiceCoordinator voiceCoordinator) {
    return new ChatViewModel(sessionManager, orchestrator, conversationRepository, voiceCoordinator);
  }
}
