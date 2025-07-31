package com.scorpio.portfoliotracker.usecase

import com.scorpio.portfoliotracker.network.NetworkResult
import com.scorpio.portfoliotracker.repository.IPortfolioRepository
import com.scorpio.portfoliotracker.usecase.PortfolioResult.Success
import com.scorpio.portfoliotracker.usecase.PortfolioResult.Failure
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

internal class GetPortfolioUsecase @Inject constructor(
    private val portfolioRepository: IPortfolioRepository,
) : IPortfolioUsecase {
    private val _portfolioResultFlow: MutableSharedFlow<PortfolioResult> = MutableSharedFlow(replay = 1)
    override val portfolioResultFlow: SharedFlow<PortfolioResult>
        get() = _portfolioResultFlow

    override suspend fun getPortfolio() {
        portfolioRepository.getPortfolio().collectLatest { data ->
            val result = when (data) {
                is NetworkResult.Error -> Failure(data.message)
                is NetworkResult.Exception -> Failure(data.e.localizedMessage)
                is NetworkResult.Success -> {
                    val userHoldings = data.data.data?.userHolding?.filterNotNull()?.map {
                        Holding(
                            it.symbol ?: "",
                            it.ltp ?: 0.0,
                            it.quantity ?: 0,
                            it.avgPrice ?: 0.0,
                            close = it.close ?: 0.0
                        )
                    }
                        ?.filter { it.symbol.isNotBlank() }
                        ?: emptyList()
                    val aggregatedData = AggregatedData(
                        currentValue = userHoldings.sumOf { it.ltp * it.netQuantity },
                        totalInvestment = userHoldings.sumOf { it.totalInvestment },
                        totalPnL = userHoldings.sumOf { it.pnl },
                        todayPnL = userHoldings.sumOf { it.todayPnl })
                    Success(holding = userHoldings, aggregatedData = aggregatedData)
                }
            }
            _portfolioResultFlow.emit(result)
        }
    }
}