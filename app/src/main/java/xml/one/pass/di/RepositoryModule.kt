package xml.one.pass.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xml.one.pass.data.preference.OnePassRepositoryImpl
import xml.one.pass.data.repository.AccountRepositoryImpl
import xml.one.pass.data.repository.PasswordRepositoryImpl
import xml.one.pass.domain.preference.OnePassRepository
import xml.one.pass.domain.repository.AccountRepository
import xml.one.pass.domain.repository.PasswordRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    fun bindPasswordRepository(
        passwordRepositoryImpl: PasswordRepositoryImpl
    ): PasswordRepository

    @Binds
    @Singleton
    fun bindOnePassRepository(
        onePassRepositoryImpl: OnePassRepositoryImpl
    ): OnePassRepository

    companion object {
        @Provides
        @Singleton
        fun provideOnePassDataStorePreferences(
            @ApplicationContext applicationContext: Context
        ): DataStore<Preferences> {
            return applicationContext.onePassDataStore
        }
    }
}
