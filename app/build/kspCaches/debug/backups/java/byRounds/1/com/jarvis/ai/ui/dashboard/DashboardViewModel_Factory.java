package com.jarvis.ai.ui.dashboard;

import com.jarvis.ai.data.local.secure.SecureKeyStore;
import com.jarvis.ai.data.repository.ReminderRepository;
import com.jarvis.ai.data.repository.SettingsRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<SecureKeyStore> secureKeyStoreProvider;

  private final Provider<ReminderRepository> reminderRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public DashboardViewModel_Factory(Provider<SecureKeyStore> secureKeyStoreProvider,
      Provider<ReminderRepository> reminderRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.secureKeyStoreProvider = secureKeyStoreProvider;
    this.reminderRepositoryProvider = reminderRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(secureKeyStoreProvider.get(), reminderRepositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<SecureKeyStore> secureKeyStoreProvider,
      Provider<ReminderRepository> reminderRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new DashboardViewModel_Factory(secureKeyStoreProvider, reminderRepositoryProvider, settingsRepositoryProvider);
  }

  public static DashboardViewModel newInstance(SecureKeyStore secureKeyStore,
      ReminderRepository reminderRepository, SettingsRepository settingsRepository) {
    return new DashboardViewModel(secureKeyStore, reminderRepository, settingsRepository);
  }
}
