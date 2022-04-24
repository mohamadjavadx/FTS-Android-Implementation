package com.mohamadjavadx.ftsandroidimplementation.di

import android.content.Context
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfigurationFactory
import com.couchbase.lite.create
import com.mohamadjavadx.ftsandroidimplementation.R
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
    fun provideCouchbaseDatabase(
        @ApplicationContext
        context: Context
    ): Database {
        CouchbaseLite.init(context)
        val appName = context.resources.getString(R.string.app_name)
        val configuration = DatabaseConfigurationFactory.create()
        return Database(appName, configuration)
    }

}