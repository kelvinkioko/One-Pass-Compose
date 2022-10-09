package xml.one.pass.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xml.one.pass.data.local.OnePassDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesStockDatabase(app: Application): OnePassDatabase {
        return Room
            .databaseBuilder(app, OnePassDatabase::class.java, "stock.db")
            .build()
    }
}
