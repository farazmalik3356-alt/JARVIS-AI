package com.jarvis.ai.action;

import com.jarvis.ai.action.handlers.AppLauncher;
import com.jarvis.ai.action.handlers.SettingsLauncher;
import com.jarvis.ai.action.handlers.ShareIntentHandler;
import com.jarvis.ai.action.handlers.UrlOpener;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ActionExecutor_Factory implements Factory<ActionExecutor> {
  private final Provider<AppLauncher> appLauncherProvider;

  private final Provider<SettingsLauncher> settingsLauncherProvider;

  private final Provider<UrlOpener> urlOpenerProvider;

  private final Provider<ShareIntentHandler> shareIntentHandlerProvider;

  private final Provider<ReminderScheduler> reminderSchedulerProvider;

  private final Provider<PlanValidator> planValidatorProvider;

  public ActionExecutor_Factory(Provider<AppLauncher> appLauncherProvider,
      Provider<SettingsLauncher> settingsLauncherProvider, Provider<UrlOpener> urlOpenerProvider,
      Provider<ShareIntentHandler> shareIntentHandlerProvider,
      Provider<ReminderScheduler> reminderSchedulerProvider,
      Provider<PlanValidator> planValidatorProvider) {
    this.appLauncherProvider = appLauncherProvider;
    this.settingsLauncherProvider = settingsLauncherProvider;
    this.urlOpenerProvider = urlOpenerProvider;
    this.shareIntentHandlerProvider = shareIntentHandlerProvider;
    this.reminderSchedulerProvider = reminderSchedulerProvider;
    this.planValidatorProvider = planValidatorProvider;
  }

  @Override
  public ActionExecutor get() {
    return newInstance(appLauncherProvider.get(), settingsLauncherProvider.get(), urlOpenerProvider.get(), shareIntentHandlerProvider.get(), reminderSchedulerProvider.get(), planValidatorProvider.get());
  }

  public static ActionExecutor_Factory create(Provider<AppLauncher> appLauncherProvider,
      Provider<SettingsLauncher> settingsLauncherProvider, Provider<UrlOpener> urlOpenerProvider,
      Provider<ShareIntentHandler> shareIntentHandlerProvider,
      Provider<ReminderScheduler> reminderSchedulerProvider,
      Provider<PlanValidator> planValidatorProvider) {
    return new ActionExecutor_Factory(appLauncherProvider, settingsLauncherProvider, urlOpenerProvider, shareIntentHandlerProvider, reminderSchedulerProvider, planValidatorProvider);
  }

  public static ActionExecutor newInstance(AppLauncher appLauncher,
      SettingsLauncher settingsLauncher, UrlOpener urlOpener, ShareIntentHandler shareIntentHandler,
      ReminderScheduler reminderScheduler, PlanValidator planValidator) {
    return new ActionExecutor(appLauncher, settingsLauncher, urlOpener, shareIntentHandler, reminderScheduler, planValidator);
  }
}
