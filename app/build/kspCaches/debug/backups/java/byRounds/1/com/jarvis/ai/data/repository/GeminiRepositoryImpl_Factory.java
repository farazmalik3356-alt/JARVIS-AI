package com.jarvis.ai.data.repository;

import com.jarvis.ai.data.remote.GeminiApi;
import com.jarvis.ai.di.AppConfig;
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
public final class GeminiRepositoryImpl_Factory implements Factory<GeminiRepositoryImpl> {
  private final Provider<GeminiApi> apiProvider;

  private final Provider<AppConfig> configProvider;

  public GeminiRepositoryImpl_Factory(Provider<GeminiApi> apiProvider,
      Provider<AppConfig> configProvider) {
    this.apiProvider = apiProvider;
    this.configProvider = configProvider;
  }

  @Override
  public GeminiRepositoryImpl get() {
    return newInstance(apiProvider.get(), configProvider.get());
  }

  public static GeminiRepositoryImpl_Factory create(Provider<GeminiApi> apiProvider,
      Provider<AppConfig> configProvider) {
    return new GeminiRepositoryImpl_Factory(apiProvider, configProvider);
  }

  public static GeminiRepositoryImpl newInstance(GeminiApi api, AppConfig config) {
    return new GeminiRepositoryImpl(api, config);
  }
}
