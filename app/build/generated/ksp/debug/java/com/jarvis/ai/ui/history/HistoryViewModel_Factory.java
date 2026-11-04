package com.jarvis.ai.ui.history;

import com.jarvis.ai.data.repository.ConversationRepository;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<ConversationRepository> conversationRepositoryProvider;

  public HistoryViewModel_Factory(Provider<ConversationRepository> conversationRepositoryProvider) {
    this.conversationRepositoryProvider = conversationRepositoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(conversationRepositoryProvider.get());
  }

  public static HistoryViewModel_Factory create(
      Provider<ConversationRepository> conversationRepositoryProvider) {
    return new HistoryViewModel_Factory(conversationRepositoryProvider);
  }

  public static HistoryViewModel newInstance(ConversationRepository conversationRepository) {
    return new HistoryViewModel(conversationRepository);
  }
}
