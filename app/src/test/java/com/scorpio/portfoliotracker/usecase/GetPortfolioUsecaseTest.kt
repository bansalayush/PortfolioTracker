package com.scorpio.portfoliotracker.usecase

import com.scorpio.portfoliotracker.data.remote.DataContent
import com.scorpio.portfoliotracker.data.remote.PortfolioData
import com.scorpio.portfoliotracker.data.remote.UserHolding
import com.scorpio.portfoliotracker.network.NetworkResult
import com.scorpio.portfoliotracker.repository.IPortfolioRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetPortfolioUsecaseTest {
    private lateinit var repository: IPortfolioRepository
    private lateinit var usecase: GetPortfolioUsecase

    @Before
    fun setUp() {
        repository = mockk()
        usecase = GetPortfolioUsecase(repository)
    }

    @Test
    fun `getPortfolio emits Success when repository returns success`() = runTest {
        val userHolding = UserHolding(
            symbol = "TCS",
            ltp = 3500.0,
            quantity = 2,
            avgPrice = 3000.0,
            close = 3400.0
        )
        val portfolioData = PortfolioData(
            data = DataContent(userHolding = listOf(userHolding))
        )
        coEvery { repository.getPortfolio() } returns flowOf(NetworkResult.Success(portfolioData))

        var result: PortfolioResult? = null
        val job = launch {
            result = usecase.portfolioResultFlow.first()
        }
        usecase.getPortfolio()
        job.join()

        assertTrue(result is PortfolioResult.Success)
        val success = result as PortfolioResult.Success
        assertEquals(1, success.holding.size)
        assertEquals("TCS", success.holding[0].symbol)
        assertEquals(3500.0, success.holding[0].ltp)
        assertEquals(2, success.holding[0].netQuantity)
        assertEquals(3000.0, success.holding[0].avgPrice)
        assertEquals(3400.0, success.holding[0].close)
        // Check aggregated data
        assertEquals(7000.0, success.aggregatedData.currentValue)
        assertEquals(6000.0, success.aggregatedData.totalInvestment)
        assertEquals(1000.0, success.aggregatedData.totalPnL)
        assertEquals(200.0, success.aggregatedData.todayPnL)
    }

    @Test
    fun `getPortfolio emits Failure when repository returns error`() = runTest {
        coEvery { repository.getPortfolio() } returns flowOf(NetworkResult.Error(code=500,"Network error"))

        var result: PortfolioResult? = null
        val job = launch {
            result = usecase.portfolioResultFlow.first()
        }
        usecase.getPortfolio()
        job.join()

        assertTrue(result is PortfolioResult.Failure)
        val failure = result as PortfolioResult.Failure
        assertEquals("Network error", failure.error)
    }

    @Test
    fun `getPortfolio emits Failure when repository returns exception`() = runTest {
        coEvery { repository.getPortfolio() } returns flowOf(NetworkResult.Exception(Exception("Something went wrong")))

        var result: PortfolioResult? = null
        val job = launch {
            result = usecase.portfolioResultFlow.first()
        }
        usecase.getPortfolio()
        job.join()

        assertTrue(result is PortfolioResult.Failure)
        val failure = result as PortfolioResult.Failure
        assertEquals("Something went wrong", failure.error)
    }
}