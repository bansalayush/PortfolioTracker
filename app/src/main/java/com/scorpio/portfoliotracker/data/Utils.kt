package com.scorpio.portfoliotracker.data

import com.scorpio.portfoliotracker.data.local.HoldingEntity
import com.scorpio.portfoliotracker.data.remote.DataContent
import com.scorpio.portfoliotracker.data.remote.PortfolioData
import com.scorpio.portfoliotracker.data.remote.UserHolding
import com.scorpio.portfoliotracker.network.NetworkResult

fun List<HoldingEntity>.toPortfolioData(): PortfolioData {
    return PortfolioData(
        data = DataContent(
            userHolding = this.map { entity ->
                UserHolding(
                    symbol = entity.symbol,
                    quantity = entity.quantity,
                    ltp = entity.ltp,
                    avgPrice = entity.avgPrice,
                    close = entity.close,
                )
            }
        )
    )
}

fun NetworkResult.Success<PortfolioData>.toHoldingEntities(): List<HoldingEntity> {
    return this.data.data?.userHolding
        ?.filterNotNull()
        ?.filter { !it.symbol.isNullOrEmpty() }
        ?.map {
            HoldingEntity(
                symbol = it.symbol!!,
                quantity = it.quantity ?: 0,
                ltp = it.ltp ?: 0.0,
                close = it.close ?: 0.0,
                avgPrice = it.avgPrice ?: 0.0,
            )
        } ?: emptyList()
}