package com.scorpio.portfoliotracker.repository

import com.scorpio.portfoliotracker.data.remote.PortfolioService
import com.scorpio.portfoliotracker.data.local.HoldingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providePortfolioRepository(portfolioService: PortfolioService, holdingDao: HoldingDao): IPortfolioRepository {
        return PortfolioRepository(portfolioService,holdingDao)
    }
}