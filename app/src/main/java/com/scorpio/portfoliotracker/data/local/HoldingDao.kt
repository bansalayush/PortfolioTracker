package com.scorpio.portfoliotracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HoldingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHolding(holding: HoldingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoldings(holdings: List<HoldingEntity>)

    @Query("SELECT * FROM holding_table")
    fun getHoldingsAsync(): Flow<List<HoldingEntity>>

    @Query("SELECT * FROM holding_table")
    suspend fun getHoldings(): List<HoldingEntity>

}