package xml.one.pass.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.onePassDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "xml.one.pass"
)
