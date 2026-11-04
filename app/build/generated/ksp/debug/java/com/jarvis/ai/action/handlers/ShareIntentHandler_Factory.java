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
public final class ShareIntentHandler_Factory implements Factory<ShareIntentHandler> {
  private final Provider<Context> contextProvider;

  public ShareIntentHandler_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ShareIntentHandler get() {
    return newInstance(contextProvider.get());
  }

  public static ShareIntentHandler_Factory create(Provider<Context> contextProvider) {
    return new ShareIntentHandler_Factory(contextProvider);
  }

  public static ShareIntentHandler newInstance(Context context) {
    return new ShareIntentHandler(context);
  }
}
