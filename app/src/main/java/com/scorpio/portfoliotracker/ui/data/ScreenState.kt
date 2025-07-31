package com.scorpio.portfoliotracker.ui.data

import com.scorpio.portfoliotracker.usecase.AggregatedData
import com.scorpio.portfoliotracker.usecase.Holding

sealed class ScreenState {
    object Loading : ScreenState()
    class Error(val error: String?, val throwable: Throwable?) : ScreenState()
    object Content : ScreenState()
}

