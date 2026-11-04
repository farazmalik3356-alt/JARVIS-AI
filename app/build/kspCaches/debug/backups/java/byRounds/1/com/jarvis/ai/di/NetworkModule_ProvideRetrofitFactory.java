package com.jarvis.ai.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.serialization.json.Json;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideRetrofitFactory implements Factory<Retrofit> {
  private final Provider<OkHttpClient> clientProvider;

  private final Provider<Json> jsonProvider;

  private final Provider<AppConfig> configProvider;

  public NetworkModule_ProvideRetrofitFactory(Provider<OkHttpClient> clientProvider,
      Provider<Json> jsonProvider, Provider<AppConfig> configProvider) {
    this.clientProvider = clientProvider;
    this.jsonProvider = jsonProvider;
    this.configProvider = configProvider;
  }

  @Override
  public Retrofit get() {
    return provideRetrofit(clientProvider.get(), jsonProvider.get(), configProvider.get());
  }

  public static NetworkModule_ProvideRetrofitFactory create(Provider<OkHttpClient> clientProvider,
      Provider<Json> jsonProvider, Provider<AppConfig> configProvider) {
    return new NetworkModule_ProvideRetrofitFactory(clientProvider, jsonProvider, configProvider);
  }

  public static Retrofit provideRetrofit(OkHttpClient client, Json json, AppConfig config) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideRetrofit(client, json, config));
  }
}
