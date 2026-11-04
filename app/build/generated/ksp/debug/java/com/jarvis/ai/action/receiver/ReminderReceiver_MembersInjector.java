package com.jarvis.ai.action.receiver;

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
public final class ReminderReceiver_MembersInjector implements MembersInjector<ReminderReceiver> {
  private final Provider<ReminderRepository> reminderRepositoryProvider;

  public ReminderReceiver_MembersInjector(Provider<ReminderRepository> reminderRepositoryProvider) {
    this.reminderRepositoryProvider = reminderRepositoryProvider;
  }

  public static MembersInjector<ReminderReceiver> create(
      Provider<ReminderRepository> reminderRepositoryProvider) {
    return new ReminderReceiver_MembersInjector(reminderRepositoryProvider);
  }

  @Override
  public void injectMembers(ReminderReceiver instance) {
    injectReminderRepository(instance, reminderRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.jarvis.ai.action.receiver.ReminderReceiver.reminderRepository")
  public static void injectReminderRepository(ReminderReceiver instance,
      ReminderRepository reminderRepository) {
    instance.reminderRepository = reminderRepository;
  }
}
