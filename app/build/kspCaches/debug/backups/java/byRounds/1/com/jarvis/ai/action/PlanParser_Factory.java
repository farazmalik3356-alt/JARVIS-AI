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
public final class PlanParser_Factory implements Factory<PlanParser> {
  @Override
  public PlanParser get() {
    return newInstance();
  }

  public static PlanParser_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PlanParser newInstance() {
    return new PlanParser();
  }

  private static final class InstanceHolder {
    private static final PlanParser_Factory INSTANCE = new PlanParser_Factory();
  }
}
