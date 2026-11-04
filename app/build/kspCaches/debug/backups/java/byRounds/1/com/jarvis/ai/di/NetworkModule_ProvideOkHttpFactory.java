package com.jarvis.ai.di;

import com.jarvis.ai.data.local.secure.SecureKeyStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class NetworkModule_ProvideOkHttpFactory implements Factory<OkHttpClient> {
  private final Provider<SecureKeyStore> secureKeyStoreProvider;

  public NetworkModule_ProvideOkHttpFactory(Provider<SecureKeyStore> secureKeyStoreProvider) {
    this.secureKeyStoreProvider = secureKeyStoreProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttp(secureKeyStoreProvider.get());
  }

  public static NetworkModule_ProvideOkHttpFactory create(
      Provider<SecureKeyStore> secureKeyStoreProvider) {
    return new NetworkModule_ProvideOkHttpFactory(secureKeyStoreProvider);
  }

  public static OkHttpClient provideOkHttp(SecureKeyStore secureKeyStore) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOkHttp(secureKeyStore));
  }
}
