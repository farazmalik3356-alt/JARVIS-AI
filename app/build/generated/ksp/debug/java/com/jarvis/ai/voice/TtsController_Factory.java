package com.jarvis.ai.voice;

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
public final class TtsController_Factory implements Factory<TtsController> {
  private final Provider<Context> contextProvider;

  public TtsController_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TtsController get() {
    return newInstance(contextProvider.get());
  }

  public static TtsController_Factory create(Provider<Context> contextProvider) {
    return new TtsController_Factory(contextProvider);
  }

  public static TtsController newInstance(Context context) {
    return new TtsController(context);
  }
}
