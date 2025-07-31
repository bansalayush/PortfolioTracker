package com.scorpio.portfoliotracker.repository

import com.scorpio.portfoliotracker.data.remote.PortfolioData
import com.scorpio.portfoliotracker.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IPortfolioRepository {
    suspend fun getPortfolio(): Flow<NetworkResult<PortfolioData>>

}