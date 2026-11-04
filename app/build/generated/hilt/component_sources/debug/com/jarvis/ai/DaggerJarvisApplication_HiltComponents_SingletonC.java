package com.jarvis.ai;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.jarvis.ai.action.ActionExecutor;
import com.jarvis.ai.action.PlanParser;
import com.jarvis.ai.action.PlanValidator;
import com.jarvis.ai.action.ReminderScheduler;
import com.jarvis.ai.action.handlers.AppLauncher;
import com.jarvis.ai.action.handlers.SettingsLauncher;
import com.jarvis.ai.action.handlers.ShareIntentHandler;
import com.jarvis.ai.action.handlers.UrlOpener;
import com.jarvis.ai.action.receiver.ReminderBootReceiver;
import com.jarvis.ai.action.receiver.ReminderBootReceiver_MembersInjector;
import com.jarvis.ai.action.receiver.ReminderReceiver;
import com.jarvis.ai.action.receiver.ReminderReceiver_MembersInjector;
import com.jarvis.ai.chat.ChatOrchestrator;
import com.jarvis.ai.chat.ChatSessionManager;
import com.jarvis.ai.data.local.db.JarvisDatabase;
import com.jarvis.ai.data.local.db.dao.ConversationDao;
import com.jarvis.ai.data.local.db.dao.MessageDao;
import com.jarvis.ai.data.local.db.dao.ReminderDao;
import com.jarvis.ai.data.local.secure.SecureKeyStore;
import com.jarvis.ai.data.remote.GeminiApi;
import com.jarvis.ai.data.repository.ConversationRepository;
import com.jarvis.ai.data.repository.ConversationRepositoryImpl;
import com.jarvis.ai.data.repository.GeminiRepository;
import com.jarvis.ai.data.repository.GeminiRepositoryImpl;
import com.jarvis.ai.data.repository.ReminderRepository;
import com.jarvis.ai.data.repository.ReminderRepositoryImpl;
import com.jarvis.ai.data.repository.SettingsRepository;
import com.jarvis.ai.data.repository.SettingsRepositoryImpl;
import com.jarvis.ai.di.AppConfig;
import com.jarvis.ai.di.AppModule_ProvideAppConfigFactory;
import com.jarvis.ai.di.AppModule_ProvideSecureKeyStoreFactory;
import com.jarvis.ai.di.AppModule_ProvideSettingsDataStoreFactory;
import com.jarvis.ai.di.DatabaseModule_ProvideConversationDaoFactory;
import com.jarvis.ai.di.DatabaseModule_ProvideDatabaseFactory;
import com.jarvis.ai.di.DatabaseModule_ProvideMessageDaoFactory;
import com.jarvis.ai.di.DatabaseModule_ProvideReminderDaoFactory;
import com.jarvis.ai.di.NetworkModule_ProvideGeminiApiFactory;
import com.jarvis.ai.di.NetworkModule_ProvideJsonFactory;
import com.jarvis.ai.di.NetworkModule_ProvideOkHttpFactory;
import com.jarvis.ai.di.NetworkModule_ProvideRetrofitFactory;
import com.jarvis.ai.ui.RootViewModel;
import com.jarvis.ai.ui.RootViewModel_HiltModules;
import com.jarvis.ai.ui.chat.ChatViewModel;
import com.jarvis.ai.ui.chat.ChatViewModel_HiltModules;
import com.jarvis.ai.ui.dashboard.DashboardViewModel;
import com.jarvis.ai.ui.dashboard.DashboardViewModel_HiltModules;
import com.jarvis.ai.ui.history.HistoryViewModel;
import com.jarvis.ai.ui.history.HistoryViewModel_HiltModules;
import com.jarvis.ai.ui.onboarding.OnboardingViewModel;
import com.jarvis.ai.ui.onboarding.OnboardingViewModel_HiltModules;
import com.jarvis.ai.ui.settings.SettingsViewModel;
import com.jarvis.ai.ui.settings.SettingsViewModel_HiltModules;
import com.jarvis.ai.ui.voice.VoiceViewModel;
import com.jarvis.ai.ui.voice.VoiceViewModel_HiltModules;
import com.jarvis.ai.voice.SpeechController;
import com.jarvis.ai.voice.TtsController;
import com.jarvis.ai.voice.VoiceCoordinator;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.serialization.json.Json;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class DaggerJarvisApplication_HiltComponents_SingletonC {
  private DaggerJarvisApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public JarvisApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements JarvisApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public JarvisApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements JarvisApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public JarvisApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements JarvisApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public JarvisApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements JarvisApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public JarvisApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements JarvisApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public JarvisApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements JarvisApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public JarvisApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements JarvisApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public JarvisApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends JarvisApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends JarvisApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends JarvisApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends JarvisApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(7).put(LazyClassKeyProvider.com_jarvis_ai_ui_chat_ChatViewModel, ChatViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_jarvis_ai_ui_dashboard_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_jarvis_ai_ui_history_HistoryViewModel, HistoryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_jarvis_ai_ui_onboarding_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_jarvis_ai_ui_RootViewModel, RootViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_jarvis_ai_ui_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_jarvis_ai_ui_voice_VoiceViewModel, VoiceViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_jarvis_ai_ui_RootViewModel = "com.jarvis.ai.ui.RootViewModel";

      static String com_jarvis_ai_ui_settings_SettingsViewModel = "com.jarvis.ai.ui.settings.SettingsViewModel";

      static String com_jarvis_ai_ui_chat_ChatViewModel = "com.jarvis.ai.ui.chat.ChatViewModel";

      static String com_jarvis_ai_ui_dashboard_DashboardViewModel = "com.jarvis.ai.ui.dashboard.DashboardViewModel";

      static String com_jarvis_ai_ui_history_HistoryViewModel = "com.jarvis.ai.ui.history.HistoryViewModel";

      static String com_jarvis_ai_ui_voice_VoiceViewModel = "com.jarvis.ai.ui.voice.VoiceViewModel";

      static String com_jarvis_ai_ui_onboarding_OnboardingViewModel = "com.jarvis.ai.ui.onboarding.OnboardingViewModel";

      @KeepFieldType
      RootViewModel com_jarvis_ai_ui_RootViewModel2;

      @KeepFieldType
      SettingsViewModel com_jarvis_ai_ui_settings_SettingsViewModel2;

      @KeepFieldType
      ChatViewModel com_jarvis_ai_ui_chat_ChatViewModel2;

      @KeepFieldType
      DashboardViewModel com_jarvis_ai_ui_dashboard_DashboardViewModel2;

      @KeepFieldType
      HistoryViewModel com_jarvis_ai_ui_history_HistoryViewModel2;

      @KeepFieldType
      VoiceViewModel com_jarvis_ai_ui_voice_VoiceViewModel2;

      @KeepFieldType
      OnboardingViewModel com_jarvis_ai_ui_onboarding_OnboardingViewModel2;
    }
  }

  private static final class ViewModelCImpl extends JarvisApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<ChatViewModel> chatViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<HistoryViewModel> historyViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<RootViewModel> rootViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<VoiceViewModel> voiceViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.chatViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.historyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.rootViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.voiceViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(7).put(LazyClassKeyProvider.com_jarvis_ai_ui_chat_ChatViewModel, ((Provider) chatViewModelProvider)).put(LazyClassKeyProvider.com_jarvis_ai_ui_dashboard_DashboardViewModel, ((Provider) dashboardViewModelProvider)).put(LazyClassKeyProvider.com_jarvis_ai_ui_history_HistoryViewModel, ((Provider) historyViewModelProvider)).put(LazyClassKeyProvider.com_jarvis_ai_ui_onboarding_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_jarvis_ai_ui_RootViewModel, ((Provider) rootViewModelProvider)).put(LazyClassKeyProvider.com_jarvis_ai_ui_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_jarvis_ai_ui_voice_VoiceViewModel, ((Provider) voiceViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_jarvis_ai_ui_voice_VoiceViewModel = "com.jarvis.ai.ui.voice.VoiceViewModel";

      static String com_jarvis_ai_ui_RootViewModel = "com.jarvis.ai.ui.RootViewModel";

      static String com_jarvis_ai_ui_onboarding_OnboardingViewModel = "com.jarvis.ai.ui.onboarding.OnboardingViewModel";

      static String com_jarvis_ai_ui_settings_SettingsViewModel = "com.jarvis.ai.ui.settings.SettingsViewModel";

      static String com_jarvis_ai_ui_history_HistoryViewModel = "com.jarvis.ai.ui.history.HistoryViewModel";

      static String com_jarvis_ai_ui_dashboard_DashboardViewModel = "com.jarvis.ai.ui.dashboard.DashboardViewModel";

      static String com_jarvis_ai_ui_chat_ChatViewModel = "com.jarvis.ai.ui.chat.ChatViewModel";

      @KeepFieldType
      VoiceViewModel com_jarvis_ai_ui_voice_VoiceViewModel2;

      @KeepFieldType
      RootViewModel com_jarvis_ai_ui_RootViewModel2;

      @KeepFieldType
      OnboardingViewModel com_jarvis_ai_ui_onboarding_OnboardingViewModel2;

      @KeepFieldType
      SettingsViewModel com_jarvis_ai_ui_settings_SettingsViewModel2;

      @KeepFieldType
      HistoryViewModel com_jarvis_ai_ui_history_HistoryViewModel2;

      @KeepFieldType
      DashboardViewModel com_jarvis_ai_ui_dashboard_DashboardViewModel2;

      @KeepFieldType
      ChatViewModel com_jarvis_ai_ui_chat_ChatViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.jarvis.ai.ui.chat.ChatViewModel 
          return (T) new ChatViewModel(singletonCImpl.chatSessionManagerProvider.get(), singletonCImpl.chatOrchestratorProvider.get(), singletonCImpl.bindConversationRepositoryProvider.get(), singletonCImpl.voiceCoordinatorProvider.get());

          case 1: // com.jarvis.ai.ui.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.provideSecureKeyStoreProvider.get(), singletonCImpl.bindReminderRepositoryProvider.get(), singletonCImpl.bindSettingsRepositoryProvider.get());

          case 2: // com.jarvis.ai.ui.history.HistoryViewModel 
          return (T) new HistoryViewModel(singletonCImpl.bindConversationRepositoryProvider.get());

          case 3: // com.jarvis.ai.ui.onboarding.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.provideSecureKeyStoreProvider.get(), singletonCImpl.bindGeminiRepositoryProvider.get(), singletonCImpl.bindSettingsRepositoryProvider.get());

          case 4: // com.jarvis.ai.ui.RootViewModel 
          return (T) new RootViewModel(singletonCImpl.bindSettingsRepositoryProvider.get());

          case 5: // com.jarvis.ai.ui.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.provideSecureKeyStoreProvider.get(), singletonCImpl.bindGeminiRepositoryProvider.get(), singletonCImpl.bindSettingsRepositoryProvider.get(), singletonCImpl.bindConversationRepositoryProvider.get(), singletonCImpl.bindReminderRepositoryProvider.get(), singletonCImpl.voiceCoordinatorProvider.get());

          case 6: // com.jarvis.ai.ui.voice.VoiceViewModel 
          return (T) new VoiceViewModel(singletonCImpl.chatSessionManagerProvider.get(), singletonCImpl.chatOrchestratorProvider.get(), singletonCImpl.bindConversationRepositoryProvider.get(), singletonCImpl.voiceCoordinatorProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends JarvisApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends JarvisApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends JarvisApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<JarvisDatabase> provideDatabaseProvider;

    private Provider<ReminderRepositoryImpl> reminderRepositoryImplProvider;

    private Provider<ReminderRepository> bindReminderRepositoryProvider;

    private Provider<ReminderScheduler> reminderSchedulerProvider;

    private Provider<ConversationRepositoryImpl> conversationRepositoryImplProvider;

    private Provider<ConversationRepository> bindConversationRepositoryProvider;

    private Provider<DataStore<Preferences>> provideSettingsDataStoreProvider;

    private Provider<SettingsRepositoryImpl> settingsRepositoryImplProvider;

    private Provider<SettingsRepository> bindSettingsRepositoryProvider;

    private Provider<ChatSessionManager> chatSessionManagerProvider;

    private Provider<SecureKeyStore> provideSecureKeyStoreProvider;

    private Provider<OkHttpClient> provideOkHttpProvider;

    private Provider<Json> provideJsonProvider;

    private Provider<AppConfig> provideAppConfigProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<GeminiApi> provideGeminiApiProvider;

    private Provider<GeminiRepositoryImpl> geminiRepositoryImplProvider;

    private Provider<GeminiRepository> bindGeminiRepositoryProvider;

    private Provider<PlanParser> planParserProvider;

    private Provider<AppLauncher> appLauncherProvider;

    private Provider<SettingsLauncher> settingsLauncherProvider;

    private Provider<UrlOpener> urlOpenerProvider;

    private Provider<ShareIntentHandler> shareIntentHandlerProvider;

    private Provider<PlanValidator> planValidatorProvider;

    private Provider<ActionExecutor> actionExecutorProvider;

    private Provider<ChatOrchestrator> chatOrchestratorProvider;

    private Provider<SpeechController> speechControllerProvider;

    private Provider<TtsController> ttsControllerProvider;

    private Provider<VoiceCoordinator> voiceCoordinatorProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);
      initialize2(applicationContextModuleParam);

    }

    private ReminderDao reminderDao() {
      return DatabaseModule_ProvideReminderDaoFactory.provideReminderDao(provideDatabaseProvider.get());
    }

    private ConversationDao conversationDao() {
      return DatabaseModule_ProvideConversationDaoFactory.provideConversationDao(provideDatabaseProvider.get());
    }

    private MessageDao messageDao() {
      return DatabaseModule_ProvideMessageDaoFactory.provideMessageDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<JarvisDatabase>(singletonCImpl, 1));
      this.reminderRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 0);
      this.bindReminderRepositoryProvider = DoubleCheck.provider((Provider) reminderRepositoryImplProvider);
      this.reminderSchedulerProvider = DoubleCheck.provider(new SwitchingProvider<ReminderScheduler>(singletonCImpl, 2));
      this.conversationRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 4);
      this.bindConversationRepositoryProvider = DoubleCheck.provider((Provider) conversationRepositoryImplProvider);
      this.provideSettingsDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 6));
      this.settingsRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 5);
      this.bindSettingsRepositoryProvider = DoubleCheck.provider((Provider) settingsRepositoryImplProvider);
      this.chatSessionManagerProvider = DoubleCheck.provider(new SwitchingProvider<ChatSessionManager>(singletonCImpl, 3));
      this.provideSecureKeyStoreProvider = DoubleCheck.provider(new SwitchingProvider<SecureKeyStore>(singletonCImpl, 12));
      this.provideOkHttpProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 11));
      this.provideJsonProvider = DoubleCheck.provider(new SwitchingProvider<Json>(singletonCImpl, 13));
      this.provideAppConfigProvider = DoubleCheck.provider(new SwitchingProvider<AppConfig>(singletonCImpl, 14));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 10));
      this.provideGeminiApiProvider = DoubleCheck.provider(new SwitchingProvider<GeminiApi>(singletonCImpl, 9));
      this.geminiRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 8);
      this.bindGeminiRepositoryProvider = DoubleCheck.provider((Provider) geminiRepositoryImplProvider);
      this.planParserProvider = DoubleCheck.provider(new SwitchingProvider<PlanParser>(singletonCImpl, 15));
      this.appLauncherProvider = DoubleCheck.provider(new SwitchingProvider<AppLauncher>(singletonCImpl, 17));
      this.settingsLauncherProvider = DoubleCheck.provider(new SwitchingProvider<SettingsLauncher>(singletonCImpl, 18));
      this.urlOpenerProvider = DoubleCheck.provider(new SwitchingProvider<UrlOpener>(singletonCImpl, 19));
      this.shareIntentHandlerProvider = DoubleCheck.provider(new SwitchingProvider<ShareIntentHandler>(singletonCImpl, 20));
      this.planValidatorProvider = DoubleCheck.provider(new SwitchingProvider<PlanValidator>(singletonCImpl, 21));
      this.actionExecutorProvider = DoubleCheck.provider(new SwitchingProvider<ActionExecutor>(singletonCImpl, 16));
    }

    @SuppressWarnings("unchecked")
    private void initialize2(final ApplicationContextModule applicationContextModuleParam) {
      this.chatOrchestratorProvider = DoubleCheck.provider(new SwitchingProvider<ChatOrchestrator>(singletonCImpl, 7));
      this.speechControllerProvider = DoubleCheck.provider(new SwitchingProvider<SpeechController>(singletonCImpl, 23));
      this.ttsControllerProvider = DoubleCheck.provider(new SwitchingProvider<TtsController>(singletonCImpl, 24));
      this.voiceCoordinatorProvider = DoubleCheck.provider(new SwitchingProvider<VoiceCoordinator>(singletonCImpl, 22));
    }

    @Override
    public void injectJarvisApplication(JarvisApplication jarvisApplication) {
    }

    @Override
    public void injectReminderBootReceiver(ReminderBootReceiver reminderBootReceiver) {
      injectReminderBootReceiver2(reminderBootReceiver);
    }

    @Override
    public void injectReminderReceiver(ReminderReceiver reminderReceiver) {
      injectReminderReceiver2(reminderReceiver);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private ReminderBootReceiver injectReminderBootReceiver2(ReminderBootReceiver instance) {
      ReminderBootReceiver_MembersInjector.injectReminderRepository(instance, bindReminderRepositoryProvider.get());
      ReminderBootReceiver_MembersInjector.injectReminderScheduler(instance, reminderSchedulerProvider.get());
      return instance;
    }

    private ReminderReceiver injectReminderReceiver2(ReminderReceiver instance2) {
      ReminderReceiver_MembersInjector.injectReminderRepository(instance2, bindReminderRepositoryProvider.get());
      return instance2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.jarvis.ai.data.repository.ReminderRepositoryImpl 
          return (T) new ReminderRepositoryImpl(singletonCImpl.reminderDao());

          case 1: // com.jarvis.ai.data.local.db.JarvisDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.jarvis.ai.action.ReminderScheduler 
          return (T) new ReminderScheduler(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.bindReminderRepositoryProvider.get());

          case 3: // com.jarvis.ai.chat.ChatSessionManager 
          return (T) new ChatSessionManager(singletonCImpl.bindConversationRepositoryProvider.get(), singletonCImpl.bindSettingsRepositoryProvider.get());

          case 4: // com.jarvis.ai.data.repository.ConversationRepositoryImpl 
          return (T) new ConversationRepositoryImpl(singletonCImpl.conversationDao(), singletonCImpl.messageDao());

          case 5: // com.jarvis.ai.data.repository.SettingsRepositoryImpl 
          return (T) new SettingsRepositoryImpl(singletonCImpl.provideSettingsDataStoreProvider.get());

          case 6: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) AppModule_ProvideSettingsDataStoreFactory.provideSettingsDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.jarvis.ai.chat.ChatOrchestrator 
          return (T) new ChatOrchestrator(singletonCImpl.bindGeminiRepositoryProvider.get(), singletonCImpl.bindConversationRepositoryProvider.get(), singletonCImpl.planParserProvider.get(), singletonCImpl.actionExecutorProvider.get());

          case 8: // com.jarvis.ai.data.repository.GeminiRepositoryImpl 
          return (T) new GeminiRepositoryImpl(singletonCImpl.provideGeminiApiProvider.get(), singletonCImpl.provideAppConfigProvider.get());

          case 9: // com.jarvis.ai.data.remote.GeminiApi 
          return (T) NetworkModule_ProvideGeminiApiFactory.provideGeminiApi(singletonCImpl.provideRetrofitProvider.get());

          case 10: // retrofit2.Retrofit 
          return (T) NetworkModule_ProvideRetrofitFactory.provideRetrofit(singletonCImpl.provideOkHttpProvider.get(), singletonCImpl.provideJsonProvider.get(), singletonCImpl.provideAppConfigProvider.get());

          case 11: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpFactory.provideOkHttp(singletonCImpl.provideSecureKeyStoreProvider.get());

          case 12: // com.jarvis.ai.data.local.secure.SecureKeyStore 
          return (T) AppModule_ProvideSecureKeyStoreFactory.provideSecureKeyStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 13: // kotlinx.serialization.json.Json 
          return (T) NetworkModule_ProvideJsonFactory.provideJson();

          case 14: // com.jarvis.ai.di.AppConfig 
          return (T) AppModule_ProvideAppConfigFactory.provideAppConfig();

          case 15: // com.jarvis.ai.action.PlanParser 
          return (T) new PlanParser();

          case 16: // com.jarvis.ai.action.ActionExecutor 
          return (T) new ActionExecutor(singletonCImpl.appLauncherProvider.get(), singletonCImpl.settingsLauncherProvider.get(), singletonCImpl.urlOpenerProvider.get(), singletonCImpl.shareIntentHandlerProvider.get(), singletonCImpl.reminderSchedulerProvider.get(), singletonCImpl.planValidatorProvider.get());

          case 17: // com.jarvis.ai.action.handlers.AppLauncher 
          return (T) new AppLauncher(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 18: // com.jarvis.ai.action.handlers.SettingsLauncher 
          return (T) new SettingsLauncher(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 19: // com.jarvis.ai.action.handlers.UrlOpener 
          return (T) new UrlOpener(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 20: // com.jarvis.ai.action.handlers.ShareIntentHandler 
          return (T) new ShareIntentHandler(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 21: // com.jarvis.ai.action.PlanValidator 
          return (T) new PlanValidator();

          case 22: // com.jarvis.ai.voice.VoiceCoordinator 
          return (T) new VoiceCoordinator(singletonCImpl.speechControllerProvider.get(), singletonCImpl.ttsControllerProvider.get());

          case 23: // com.jarvis.ai.voice.SpeechController 
          return (T) new SpeechController(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 24: // com.jarvis.ai.voice.TtsController 
          return (T) new TtsController(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
