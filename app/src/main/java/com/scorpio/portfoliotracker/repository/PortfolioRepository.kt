package com.scorpio.portfoliotracker.repository

import com.scorpio.portfoliotracker.data.remote.PortfolioData
import com.scorpio.portfoliotracker.data.remote.PortfolioService
import com.scorpio.portfoliotracker.data.local.HoldingDao
import com.scorpio.portfoliotracker.data.toHoldingEntities
import com.scorpio.portfoliotracker.data.toPortfolioData
import com.scorpio.portfoliotracker.network.NetworkResult
import com.scorpio.portfoliotracker.network.handleApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class PortfolioRepository @Inject constructor(
    private val portfolioService: PortfolioService,
    private val holdingDao: HoldingDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : IPortfolioRepository {
    override suspend fun getPortfolio(): Flow<NetworkResult<PortfolioData>> {

        return flow {
            val network = scope.async {
                handleApi { portfolioService.getPortfolio() }
            }
            val local = scope.async {
                holdingDao.getHoldings()
            }

            val localData = local.await()

            if (localData.isNotEmpty()) {
                emit(NetworkResult.Success(localData.toPortfolioData()))
            }


            val networkData = network.await()
            scope.launch {
                if (networkData is NetworkResult.Success) {
                    holdingDao.insertHoldings(networkData.toHoldingEntities())
                }
            }
            //optimize: check diff with local data and then emit
            if (networkData is NetworkResult.Success) {
                emit(networkData)
            } else if(localData.isEmpty()){
                emit(networkData)
            }
        }
    }
}

