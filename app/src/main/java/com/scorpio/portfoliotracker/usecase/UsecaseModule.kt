package com.scorpio.portfoliotracker.usecase

import com.scorpio.portfoliotracker.repository.IPortfolioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UsecaseModule {

    @Provides
    fun provideGetPortfolioUseCase(
        portfolioRepository: IPortfolioRepository
    ): IPortfolioUsecase {
        return GetPortfolioUsecase(portfolioRepository)
    }
}