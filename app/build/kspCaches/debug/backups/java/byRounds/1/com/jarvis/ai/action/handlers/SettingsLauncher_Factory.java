package com.jarvis.ai.action.handlers;

import android.content.Context;
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
public final class SettingsLauncher_Factory implements Factory<SettingsLauncher> {
  private final Provider<Context> contextProvider;

  public SettingsLauncher_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SettingsLauncher get() {
    return newInstance(contextProvider.get());
  }

  public static SettingsLauncher_Factory create(Provider<Context> contextProvider) {
    return new SettingsLauncher_Factory(contextProvider);
  }

  public static SettingsLauncher newInstance(Context context) {
    return new SettingsLauncher(context);
  }
}
