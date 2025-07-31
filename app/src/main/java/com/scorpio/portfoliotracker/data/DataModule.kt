package com.scorpio.portfoliotracker.data

import android.content.Context
import com.scorpio.portfoliotracker.db.AppDatabase
import com.scorpio.portfoliotracker.data.local.HoldingDao
import com.scorpio.portfoliotracker.data.remote.PortfolioService
import com.scorpio.portfoliotracker.network.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun providePortfolioService(): PortfolioService {
        return NetworkService.getService(PortfolioService::class.java)
    }

    @Provides
    fun providePortfolioDao(@ApplicationContext applicationContext: Context): HoldingDao {
        return AppDatabase.getDatabase(applicationContext).holdingDao()
    }
}