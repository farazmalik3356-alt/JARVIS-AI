package com.jarvis.ai.chat;

import com.jarvis.ai.action.ActionExecutor;
import com.jarvis.ai.action.PlanParser;
import com.jarvis.ai.data.repository.ConversationRepository;
import com.jarvis.ai.data.repository.GeminiRepository;
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
public final class ChatOrchestrator_Factory implements Factory<ChatOrchestrator> {
  private final Provider<GeminiRepository> geminiRepositoryProvider;

  private final Provider<ConversationRepository> conversationRepositoryProvider;

  private final Provider<PlanParser> planParserProvider;

  private final Provider<ActionExecutor> actionExecutorProvider;

  public ChatOrchestrator_Factory(Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<PlanParser> planParserProvider, Provider<ActionExecutor> actionExecutorProvider) {
    this.geminiRepositoryProvider = geminiRepositoryProvider;
    this.conversationRepositoryProvider = conversationRepositoryProvider;
    this.planParserProvider = planParserProvider;
    this.actionExecutorProvider = actionExecutorProvider;
  }

  @Override
  public ChatOrchestrator get() {
    return newInstance(geminiRepositoryProvider.get(), conversationRepositoryProvider.get(), planParserProvider.get(), actionExecutorProvider.get());
  }

  public static ChatOrchestrator_Factory create(Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<ConversationRepository> conversationRepositoryProvider,
      Provider<PlanParser> planParserProvider, Provider<ActionExecutor> actionExecutorProvider) {
    return new ChatOrchestrator_Factory(geminiRepositoryProvider, conversationRepositoryProvider, planParserProvider, actionExecutorProvider);
  }

  public static ChatOrchestrator newInstance(GeminiRepository geminiRepository,
      ConversationRepository conversationRepository, PlanParser planParser,
      ActionExecutor actionExecutor) {
    return new ChatOrchestrator(geminiRepository, conversationRepository, planParser, actionExecutor);
  }
}
