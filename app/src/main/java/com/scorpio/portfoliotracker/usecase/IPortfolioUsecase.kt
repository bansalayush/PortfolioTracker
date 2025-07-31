package com.scorpio.portfoliotracker.usecase

import kotlinx.coroutines.flow.SharedFlow

interface IPortfolioUsecase {
    val portfolioResultFlow: SharedFlow<PortfolioResult>
    suspend fun getPortfolio()
}


sealed class PortfolioResult {
    class Success(val holding: List<Holding>, val aggregatedData: AggregatedData) :
        PortfolioResult()

    class Failure(val error: String?) : PortfolioResult()
}

data class Holding(val symbol: String, val ltp: Double, val netQuantity: Int, val avgPrice:Double, val close:Double){
    val pnl: Double = (ltp - avgPrice) * netQuantity
    val totalInvestment: Double = avgPrice * netQuantity
    val todayPnl: Double = (ltp - close) * netQuantity
}
data class AggregatedData(
    val currentValue: Double,
    val totalInvestment: Double,
    val totalPnL: Double,
    val todayPnL: Double
)