package com.scorpio.portfoliotracker.repository

import com.scorpio.portfoliotracker.data.remote.PortfolioData

sealed class PortfolioResponse {
    class Success(val data: PortfolioData) : PortfolioResponse()
    class Error(val error: String) : PortfolioResponse()
}