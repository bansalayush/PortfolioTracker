package com.scorpio.portfoliotracker.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.scorpio.portfoliotracker.MainViewModel
import com.scorpio.portfoliotracker.ui.data.ScreenState

@Composable
fun PortfolioScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.getPortfolio()
    }
    SideEffect {
        println("SideEffect executed")
    }
    val screenState = viewModel.screenStateFlow.collectAsStateWithLifecycle()
    when (val state = screenState.value) {
        ScreenState.Content -> UserPortfolioScreen(
            viewModel.holdingScreenDataFlow,
            onHoldingClick = { ticker ->
                viewModel.onHoldingClick(ticker)
            },
            toggleAggregateView = {
                viewModel.toggleAggregateView()
            }
        )

        is ScreenState.Error -> MError(state.error, onRefresh = {
            viewModel.getPortfolio()
        })

        ScreenState.Loading -> MProgressIndicator()
    }
}

@Composable
fun MError(errorText: String?, onRefresh: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = errorText ?: "Ooop!!! This should not have happened."
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onRefresh) {
                Text("Refresh")
            }

        }
    }
}

@Composable
fun MProgressIndicator() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }

}