package com.jarvis.ai.voice;

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
public final class VoiceCoordinator_Factory implements Factory<VoiceCoordinator> {
  private final Provider<SpeechController> speechControllerProvider;

  private final Provider<TtsController> ttsControllerProvider;

  public VoiceCoordinator_Factory(Provider<SpeechController> speechControllerProvider,
      Provider<TtsController> ttsControllerProvider) {
    this.speechControllerProvider = speechControllerProvider;
    this.ttsControllerProvider = ttsControllerProvider;
  }

  @Override
  public VoiceCoordinator get() {
    return newInstance(speechControllerProvider.get(), ttsControllerProvider.get());
  }

  public static VoiceCoordinator_Factory create(Provider<SpeechController> speechControllerProvider,
      Provider<TtsController> ttsControllerProvider) {
    return new VoiceCoordinator_Factory(speechControllerProvider, ttsControllerProvider);
  }

  public static VoiceCoordinator newInstance(SpeechController speechController,
      TtsController ttsController) {
    return new VoiceCoordinator(speechController, ttsController);
  }
}
