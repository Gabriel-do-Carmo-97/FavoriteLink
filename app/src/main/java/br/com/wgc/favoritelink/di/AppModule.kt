package br.com.wgc.favoritelink.di

import android.content.Context
import androidx.room.Room
import br.com.wgc.favoritelink.data.api.LinkApi
import br.com.wgc.favoritelink.data.local.dao.LinkDao
import br.com.wgc.favoritelink.data.local.db.AppDatabase
import br.com.wgc.favoritelink.data.repository.LinkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providersAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "app_database"
    ).build()

    @Provides
    @Singleton
    fun providesLinkDao(
        appDatabase: AppDatabase
    ) = appDatabase.linkDao()

    @Provides
    @Singleton
    fun providerRepository(
        linkApi: LinkApi,
        linkDao: LinkDao
    ) = LinkRepository(linkApi, linkDao)
}