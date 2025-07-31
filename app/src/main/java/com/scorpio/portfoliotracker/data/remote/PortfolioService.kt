package com.scorpio.portfoliotracker.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface PortfolioService {
    @GET("/p1124/portfolio")
    suspend fun getPortfolio(): Response<PortfolioData>

    @GET("/p1124/portfolio")
    suspend fun asd(): PortfolioData
}