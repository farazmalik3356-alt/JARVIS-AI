package com.jarvis.ai.di;

import com.jarvis.ai.data.local.db.JarvisDatabase;
import com.jarvis.ai.data.local.db.dao.ConversationDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideConversationDaoFactory implements Factory<ConversationDao> {
  private final Provider<JarvisDatabase> dbProvider;

  public DatabaseModule_ProvideConversationDaoFactory(Provider<JarvisDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ConversationDao get() {
    return provideConversationDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideConversationDaoFactory create(
      Provider<JarvisDatabase> dbProvider) {
    return new DatabaseModule_ProvideConversationDaoFactory(dbProvider);
  }

  public static ConversationDao provideConversationDao(JarvisDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideConversationDao(db));
  }
}
