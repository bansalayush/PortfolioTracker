package com.scorpio.portfoliotracker.ui.data

import com.scorpio.portfoliotracker.usecase.AggregatedData
import com.scorpio.portfoliotracker.usecase.Holding

data class UserHoldingScreenData(
    val userHolding: List<Holding>,
    val aggregatedData: AggregatedData,
    val bottomViewState: BottomViewState = BottomViewState.collapsed
)

enum class BottomViewState {
    expanded, collapsed;

    fun isExpanded() = this == expanded
    fun isCollapsed() = this == collapsed
    fun toggle(): BottomViewState = if (isExpanded()) collapsed else expanded
}

@JvmInline
value class Ticker(val value: String)
