package com.githubrepos.trending.common.hilt

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.githubrepos.trending.common.util.CacheSession
import com.githubrepos.trending.repos.data.RepositoriesScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    private const val PREFERENCES_NAME = "com.github.trending"

    @Provides
    fun provideSettingsPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Provides
    fun providesWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

    @Provides
    fun providesCacheSession(preferences: SharedPreferences) = CacheSession(preferences = preferences)

    @Provides
    fun providesRepositoriesScheduler(workManager: WorkManager) = RepositoriesScheduler(workManager = workManager)

}