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
public final class UrlOpener_Factory implements Factory<UrlOpener> {
  private final Provider<Context> contextProvider;

  public UrlOpener_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public UrlOpener get() {
    return newInstance(contextProvider.get());
  }

  public static UrlOpener_Factory create(Provider<Context> contextProvider) {
    return new UrlOpener_Factory(contextProvider);
  }

  public static UrlOpener newInstance(Context context) {
    return new UrlOpener(context);
  }
}
