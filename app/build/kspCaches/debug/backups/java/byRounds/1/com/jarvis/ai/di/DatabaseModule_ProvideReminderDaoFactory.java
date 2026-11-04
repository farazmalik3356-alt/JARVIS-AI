package com.jarvis.ai.di;

import com.jarvis.ai.data.local.db.JarvisDatabase;
import com.jarvis.ai.data.local.db.dao.ReminderDao;
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
public final class DatabaseModule_ProvideReminderDaoFactory implements Factory<ReminderDao> {
  private final Provider<JarvisDatabase> dbProvider;

  public DatabaseModule_ProvideReminderDaoFactory(Provider<JarvisDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ReminderDao get() {
    return provideReminderDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideReminderDaoFactory create(
      Provider<JarvisDatabase> dbProvider) {
    return new DatabaseModule_ProvideReminderDaoFactory(dbProvider);
  }

  public static ReminderDao provideReminderDao(JarvisDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideReminderDao(db));
  }
}
