package com.jarvis.ai.action.receiver;

import com.jarvis.ai.action.ReminderScheduler;
import com.jarvis.ai.data.repository.ReminderRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ReminderBootReceiver_MembersInjector implements MembersInjector<ReminderBootReceiver> {
  private final Provider<ReminderRepository> reminderRepositoryProvider;

  private final Provider<ReminderScheduler> reminderSchedulerProvider;

  public ReminderBootReceiver_MembersInjector(
      Provider<ReminderRepository> reminderRepositoryProvider,
      Provider<ReminderScheduler> reminderSchedulerProvider) {
    this.reminderRepositoryProvider = reminderRepositoryProvider;
    this.reminderSchedulerProvider = reminderSchedulerProvider;
  }

  public static MembersInjector<ReminderBootReceiver> create(
      Provider<ReminderRepository> reminderRepositoryProvider,
      Provider<ReminderScheduler> reminderSchedulerProvider) {
    return new ReminderBootReceiver_MembersInjector(reminderRepositoryProvider, reminderSchedulerProvider);
  }

  @Override
  public void injectMembers(ReminderBootReceiver instance) {
    injectReminderRepository(instance, reminderRepositoryProvider.get());
    injectReminderScheduler(instance, reminderSchedulerProvider.get());
  }

  @InjectedFieldSignature("com.jarvis.ai.action.receiver.ReminderBootReceiver.reminderRepository")
  public static void injectReminderRepository(ReminderBootReceiver instance,
      ReminderRepository reminderRepository) {
    instance.reminderRepository = reminderRepository;
  }

  @InjectedFieldSignature("com.jarvis.ai.action.receiver.ReminderBootReceiver.reminderScheduler")
  public static void injectReminderScheduler(ReminderBootReceiver instance,
      ReminderScheduler reminderScheduler) {
    instance.reminderScheduler = reminderScheduler;
  }
}
