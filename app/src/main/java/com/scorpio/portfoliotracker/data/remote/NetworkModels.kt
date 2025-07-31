package com.scorpio.portfoliotracker.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PortfolioData(
    @Json(name = "data")
    val data: DataContent?
)

@JsonClass(generateAdapter = true)
data class DataContent(
    @Json(name = "userHolding")
    val userHolding: List<UserHolding?>?
)

@JsonClass(generateAdapter = true)
data class UserHolding(
    @Json(name = "symbol")
    val symbol: String?,
    @Json(name = "quantity")
    val quantity: Int?,
    @Json(name = "ltp")
    val ltp: Double?,
    @Json(name = "avgPrice")
    val avgPrice: Double?,
    @Json(name = "close")
    val close: Double?
)
