package com.jarvis.ai.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jarvis.ai.BuildConfig
import com.jarvis.ai.data.local.secure.SecureKeyStore
import com.jarvis.ai.data.local.secure.SecureKeyStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "jarvis_settings"
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.settingsDataStore

    @Provides
    @Singleton
    fun provideSecureKeyStore(
        @ApplicationContext context: Context
    ): SecureKeyStore = SecureKeyStoreImpl(context)

    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig = AppConfig(
        geminiModel = "nvidia/nemotron-3.5-super-vl:free",
        geminiBaseUrl = "https://openrouter.ai/api/v1/",
        isDebug = BuildConfig.DEBUG
    )
}

data class AppConfig(
    val geminiModel: String,
    val geminiBaseUrl: String,
    val isDebug: Boolean
)