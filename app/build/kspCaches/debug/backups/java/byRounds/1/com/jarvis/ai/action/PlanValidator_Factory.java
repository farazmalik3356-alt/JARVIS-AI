package com.jarvis.ai.action;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class PlanValidator_Factory implements Factory<PlanValidator> {
  @Override
  public PlanValidator get() {
    return newInstance();
  }

  public static PlanValidator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PlanValidator newInstance() {
    return new PlanValidator();
  }

  private static final class InstanceHolder {
    private static final PlanValidator_Factory INSTANCE = new PlanValidator_Factory();
  }
}
