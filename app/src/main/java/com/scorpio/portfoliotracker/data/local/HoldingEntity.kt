package com.scorpio.portfoliotracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holding_table")
data class HoldingEntity(
    @PrimaryKey
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
)