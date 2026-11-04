package com.jarvis.ai.ui;

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
public final class RootViewModel_Factory implements Factory<RootViewModel> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public RootViewModel_Factory(Provider<SettingsRepository> settingsRepositoryProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public RootViewModel get() {
    return newInstance(settingsRepositoryProvider.get());
  }

  public static RootViewModel_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new RootViewModel_Factory(settingsRepositoryProvider);
  }

  public static RootViewModel newInstance(SettingsRepository settingsRepository) {
    return new RootViewModel(settingsRepository);
  }
}
