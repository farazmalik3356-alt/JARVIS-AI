package com.jarvis.ai.action;

import android.content.Context;
import com.jarvis.ai.data.repository.ReminderRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ReminderScheduler_Factory implements Factory<ReminderScheduler> {
  private final Provider<Context> contextProvider;

  private final Provider<ReminderRepository> reminderRepositoryProvider;

  public ReminderScheduler_Factory(Provider<Context> contextProvider,
      Provider<ReminderRepository> reminderRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.reminderRepositoryProvider = reminderRepositoryProvider;
  }

  @Override
  public ReminderScheduler get() {
    return newInstance(contextProvider.get(), reminderRepositoryProvider.get());
  }

  public static ReminderScheduler_Factory create(Provider<Context> contextProvider,
      Provider<ReminderRepository> reminderRepositoryProvider) {
    return new ReminderScheduler_Factory(contextProvider, reminderRepositoryProvider);
  }

  public static ReminderScheduler newInstance(Context context,
      ReminderRepository reminderRepository) {
    return new ReminderScheduler(context, reminderRepository);
  }
}
