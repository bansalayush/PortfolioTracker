package com.scorpio.portfoliotracker.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.scorpio.portfoliotracker.ui.data.BottomViewState
import com.scorpio.portfoliotracker.ui.data.Ticker
import com.scorpio.portfoliotracker.ui.data.UserHoldingScreenData
import com.scorpio.portfoliotracker.ui.utils.formatMoney
import com.scorpio.portfoliotracker.usecase.AggregatedData
import com.scorpio.portfoliotracker.usecase.Holding
import kotlinx.coroutines.flow.StateFlow

@Composable
fun UserPortfolioScreen(
    holdingScreenDataFlow: StateFlow<UserHoldingScreenData?>,
    onHoldingClick: (Ticker) -> Unit,
    toggleAggregateView: () -> Unit
) {
    val userHolding by holdingScreenDataFlow.collectAsStateWithLifecycle()
    SideEffect {
        println("UserPortfolioScreen re-composed")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column {
            Holdings(modifier = Modifier.weight(1f), userHolding?.userHolding ?: emptyList())
            userHolding?.aggregatedData?.let {
                AggregatedView(
                    onToggle = toggleAggregateView,
                    it,
                    userHolding?.bottomViewState ?: BottomViewState.expanded
                )
            }
        }
    }

}

@Composable
fun AggregatedView(
    onToggle: () -> Unit,
    aggregatedData: AggregatedData,
    state: BottomViewState
) {
    val pnl = aggregatedData.totalPnL
    val pnlColor = if (pnl >= 0) Color(0xFF1DBA72) else Color(0xFFBA1D1D)
    val todayPnlColor = if (aggregatedData.todayPnL >= 0) Color(0xFF1DBA72) else Color(0xFFBA1D1D)
    val percentage = if (aggregatedData.totalInvestment > 0)
        (pnl * 100) / aggregatedData.totalInvestment
    else 0.0
    val percentageText = String.format("(%.2f%%)", percentage)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavigationBarDefaults.containerColor)
            .padding(16.dp)
            .clickable(onClick = onToggle)
    ) {
        if (state.isExpanded()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Current value*",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "₹ ${aggregatedData.currentValue.formatMoney()}",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Total investment*",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "₹ ${aggregatedData.totalInvestment.formatMoney()}",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Today's Profit & Loss*",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    (if (aggregatedData.todayPnL >= 0) "₹${aggregatedData.todayPnL.formatMoney()}" else "-₹${(-aggregatedData.todayPnL).formatMoney()}"),
                    color = todayPnlColor,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Profit & Loss*",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    (if (pnl >= 0) "₹${pnl.formatMoney()}" else "-₹${(-pnl).formatMoney()}"),
                    color = pnlColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    " $percentageText",
                    color = pnlColor,
                    style = MaterialTheme.typography.bodySmall
                )
                Icon(
                    imageVector = if (state.isExpanded()) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun Holdings(
    modifier: Modifier,
    holdings: List<Holding>,
) {
    LaunchedEffect(holdings) {
        println("change in holding detected")
    }
    LazyColumn(modifier = modifier) {
        items(
            items = holdings,
            key = { holding -> holding.symbol },
            contentType = { it.symbol }) { h ->
            HoldingListItem(h)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
fun HoldingListItem(holding: Holding) {
    val isPositive = holding.pnl >= 0
    Column(modifier = Modifier.padding(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = holding.symbol,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "LTP: ₹ ${holding.ltp.formatMoney()}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "NET QTY: ${holding.netQuantity}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "P&L: ₹ ${holding.pnl.formatMoney()}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isPositive) Color(0xFF1DBA72) else Color(0xFFBA1D1D), // green
                fontWeight = FontWeight.Bold
            )
        }
    }
}