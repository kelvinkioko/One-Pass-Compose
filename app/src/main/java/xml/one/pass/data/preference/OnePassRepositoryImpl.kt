package xml.one.pass.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import xml.one.pass.domain.preference.OnePassRepository
import java.io.IOException
import javax.inject.Inject

class OnePassRepositoryImpl @Inject constructor(
    private val onePassDataStorePref: DataStore<Preferences>
) : OnePassRepository {
    override suspend fun setLoginStatus(isLoggedIn: Boolean) {
        Result.runCatching {
            onePassDataStorePref.edit { preferences ->
                preferences[LOGIN_STATUS] = isLoggedIn
            }
        }
    }

    override suspend fun getLoginStatus(): Result<Boolean> {
        return Result.runCatching {
            val flow = onePassDataStorePref.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[LOGIN_STATUS]
                }

            val isLoggedIn = flow.firstOrNull() ?: false
            isLoggedIn
        }
    }

    private companion object {
        val LOGIN_STATUS = booleanPreferencesKey(name = "login_status")
    }
}
