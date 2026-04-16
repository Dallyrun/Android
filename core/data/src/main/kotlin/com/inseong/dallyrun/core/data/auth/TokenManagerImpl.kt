package com.inseong.dallyrun.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.inseong.dallyrun.core.network.TokenProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

internal class TokenManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : TokenProvider {

    private val cachedAccessToken = AtomicReference<String?>(null)

    override suspend fun getAccessToken(): String? {
        cachedAccessToken.get()?.let { return it }
        val stored = dataStore.data.first()[ACCESS_TOKEN_KEY]
        cachedAccessToken.set(stored)
        return stored
    }

    override suspend fun getRefreshToken(): String? =
        dataStore.data.first()[REFRESH_TOKEN_KEY]

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        cachedAccessToken.set(accessToken)
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun clearTokens() {
        cachedAccessToken.set(null)
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    internal fun isLoggedIn() = dataStore.data.map { prefs ->
        prefs[REFRESH_TOKEN_KEY] != null
    }

    private companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
}
