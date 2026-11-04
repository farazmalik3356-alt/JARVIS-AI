package com.jarvis.ai.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideAppConfigFactory implements Factory<AppConfig> {
  @Override
  public AppConfig get() {
    return provideAppConfig();
  }

  public static AppModule_ProvideAppConfigFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AppConfig provideAppConfig() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAppConfig());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideAppConfigFactory INSTANCE = new AppModule_ProvideAppConfigFactory();
  }
}
