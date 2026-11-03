package com.jarvis.ai.data.local.secure

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureKeyStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SecureKeyStore {

    private val prefs = runCatching {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }.getOrElse {
        Log.e(TAG, "EncryptedSharedPreferences init failed; falling back to in-memory only")
        null
    }

    @Synchronized
    override fun saveApiKey(key: String) {
        val trimmed = key.trim()
        require(trimmed.isNotEmpty()) { "API key cannot be blank" }
        val encrypted = encrypt(trimmed) ?: trimmed
        prefs?.edit()?.putString(KEY_API, encrypted)?.apply()
    }

    override fun readApiKey(): String? {
        val stored = prefs?.getString(KEY_API, null) ?: return null
        val decrypted = decrypt(stored) ?: stored
        return decrypted.takeIf { it.isNotBlank() }
    }

    @Synchronized
    override fun clearApiKey() {
        prefs?.edit()?.remove(KEY_API)?.apply()
    }

    override fun hasApiKey(): Boolean = !readApiKey().isNullOrBlank()

    private fun getOrCreateKey(): SecretKey? = runCatching {
        val ks = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        (ks.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.secretKey
            ?: run {
                val generator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE
                )
                generator.init(
                    KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(256)
                        .build()
                )
                generator.generateKey()
            }
    }.getOrNull()

    private fun encrypt(plain: String): String? = runCatching {
        val key = getOrCreateKey() ?: return null
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val ct = cipher.doFinal(plain.toByteArray(Charsets.UTF_8))
        Base64.encodeToString(iv, Base64.NO_WRAP) + ":" +
            Base64.encodeToString(ct, Base64.NO_WRAP)
    }.getOrNull()

    private fun decrypt(payload: String): String? = runCatching {
        val parts = payload.split(":")
        if (parts.size != 2) return null
        val key = getOrCreateKey() ?: return null
        val iv = Base64.decode(parts[0], Base64.NO_WRAP)
        val ct = Base64.decode(parts[1], Base64.NO_WRAP)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        String(cipher.doFinal(ct), Charsets.UTF_8)
    }.getOrNull()

    companion object {
        private const val TAG = "SecureKeyStore"
        private const val PREFS_NAME = "jarvis_secure_prefs"
        private const val KEY_API = "gemini_api_key"
        private const val KEY_ALIAS = "jarvis_master_key"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
    }
}