package com.jarvis.ai.ui.onboarding;

import com.jarvis.ai.data.local.secure.SecureKeyStore;
import com.jarvis.ai.data.repository.GeminiRepository;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<SecureKeyStore> secureKeyStoreProvider;

  private final Provider<GeminiRepository> geminiRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public OnboardingViewModel_Factory(Provider<SecureKeyStore> secureKeyStoreProvider,
      Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.secureKeyStoreProvider = secureKeyStoreProvider;
    this.geminiRepositoryProvider = geminiRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(secureKeyStoreProvider.get(), geminiRepositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static OnboardingViewModel_Factory create(Provider<SecureKeyStore> secureKeyStoreProvider,
      Provider<GeminiRepository> geminiRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new OnboardingViewModel_Factory(secureKeyStoreProvider, geminiRepositoryProvider, settingsRepositoryProvider);
  }

  public static OnboardingViewModel newInstance(SecureKeyStore secureKeyStore,
      GeminiRepository geminiRepository, SettingsRepository settingsRepository) {
    return new OnboardingViewModel(secureKeyStore, geminiRepository, settingsRepository);
  }
}
