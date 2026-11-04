package com.jarvis.ai.ui.voice;

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
public final class VoiceViewModel_Factory implements Factory<VoiceViewModel> {
  private final Provider<ChatSessionManager> sessionManagerProvider;

  private final Provider<ChatOrchestrator> orchestratorProvider;

  private final Provider<ConversationRepository> conversationRepositoryProvider;

  private final Provider<VoiceCoordinator> voiceCoordinatorProvider;

  public VoiceViewModel_Factory(Provider<ChatSessionManager> sessionManagerProvider,
      Provider<ChatOrchestrator> orchestratorProvider,
      Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<VoiceCoordinator> voiceCoordinatorProvider) {
    this.sessionManagerProvider = sessionManagerProvider;
    this.orchestratorProvider = orchestratorProvider;
    this.conversationRepositoryProvider = conversationRepositoryProvider;
    this.voiceCoordinatorProvider = voiceCoordinatorProvider;
  }

  @Override
  public VoiceViewModel get() {
    return newInstance(sessionManagerProvider.get(), orchestratorProvider.get(), conversationRepositoryProvider.get(), voiceCoordinatorProvider.get());
  }

  public static VoiceViewModel_Factory create(Provider<ChatSessionManager> sessionManagerProvider,
      Provider<ChatOrchestrator> orchestratorProvider,
      Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<VoiceCoordinator> voiceCoordinatorProvider) {
    return new VoiceViewModel_Factory(sessionManagerProvider, orchestratorProvider, conversationRepositoryProvider, voiceCoordinatorProvider);
  }

  public static VoiceViewModel newInstance(ChatSessionManager sessionManager,
      ChatOrchestrator orchestrator, ConversationRepository conversationRepository,
      VoiceCoordinator voiceCoordinator) {
    return new VoiceViewModel(sessionManager, orchestrator, conversationRepository, voiceCoordinator);
  }
}
