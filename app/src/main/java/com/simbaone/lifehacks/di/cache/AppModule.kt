package com.simbaone.lifehacks.di.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import com.simbaone.lifehacks.data.cache.DataStoreHelper
import com.simbaone.lifehacks.data.repo.CacheRepoImpl
import com.simbaone.lifehacks.data.repo.DataRepoImpl
import com.simbaone.lifehacks.data.repo.LogRepoImpl
import com.simbaone.lifehacks.domain.repo.CacheRepo
import com.simbaone.lifehacks.domain.repo.DataRepo
import com.simbaone.lifehacks.domain.repo.LogRepo
import com.simbaone.lifehacks.utils.AssetReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }

    @Singleton
    @Provides
    fun providePrefDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
//        migrations = listOf(SharedPreferencesMigration(appContext,USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile("USER_PREFERENCES") }
        )
    }


    @Singleton
    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences("PrefKeys.PREFERENCE_NAME", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDataStoreHelper(prefDataStore: DataStore<Preferences>): DataStoreHelper {
        return DataStoreHelper(prefDataStore)
    }

    @Singleton
    @Provides
    fun provideLocalSource(localSourceImpl: CacheRepoImpl): CacheRepo = localSourceImpl

    @Singleton
    @Provides
    fun provideDataSource(dataSourceImpl: DataRepoImpl): DataRepo = dataSourceImpl

    @Singleton
    @Provides
    fun provideLogger() : LogRepo = LogRepoImpl()

    @Singleton
    @Provides
    fun provideGson() : Gson = Gson()

    @Singleton
    @Provides
    fun provideAssetReader(context: Context) : AssetReader = AssetReader(context)

}