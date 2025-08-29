package com.scorpio.portfoliotracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scorpio.portfoliotracker.ui.data.ScreenState
import com.scorpio.portfoliotracker.ui.data.Ticker
import com.scorpio.portfoliotracker.ui.data.UserHoldingScreenData
import com.scorpio.portfoliotracker.usecase.IPortfolioUsecase
import com.scorpio.portfoliotracker.usecase.PortfolioResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val portfolioUsecase: IPortfolioUsecase
) : ViewModel() {

    private val _screenStateFlow: MutableStateFlow<ScreenState> =
        MutableStateFlow(ScreenState.Loading)
    val screenStateFlow: StateFlow<ScreenState> = _screenStateFlow

    private val _holdingScreenDataFlow: MutableStateFlow<UserHoldingScreenData?> =
        MutableStateFlow(null)
    val holdingScreenDataFlow = _holdingScreenDataFlow.asStateFlow()

    init {
        viewModelScope.launch {
            portfolioUsecase.portfolioResultFlow.collect { result ->
                when (result) {
                    is PortfolioResult.Failure -> {
                        _screenStateFlow.value = ScreenState.Error(result.error, throwable = null)
                    }

                    is PortfolioResult.Success -> {
                        _screenStateFlow.value = ScreenState.Content
                        _holdingScreenDataFlow.value = UserHoldingScreenData(
                            userHolding = result.holding,
                            aggregatedData = result.aggregatedData
                        )
                    }
                }

            }
        }
    }

    var getPortfolioJob: Job? = null
    fun getPortfolio() {
        getPortfolioJob?.cancel()
        getPortfolioJob = viewModelScope.launch(Dispatchers.IO) {
            _screenStateFlow.value = ScreenState.Loading
            portfolioUsecase.getPortfolio()
        }
    }

    fun onHoldingClick(ticker: Ticker) {
        println("Ticker clicked: $ticker")
    }

    fun toggleAggregateView() {
        val toggledState = _holdingScreenDataFlow.value!!.bottomViewState.toggle()

        _holdingScreenDataFlow.value = _holdingScreenDataFlow.value?.copy(
            bottomViewState = toggledState)
        println("toggled") }
}
