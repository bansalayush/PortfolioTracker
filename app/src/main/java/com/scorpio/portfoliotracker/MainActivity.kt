package com.scorpio.portfoliotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scorpio.portfoliotracker.ui.composables.PortfolioScreen
import com.scorpio.portfoliotracker.ui.composables.TabView
import com.scorpio.portfoliotracker.ui.data.homeTab
import com.scorpio.portfoliotracker.ui.data.portfolioTab
import com.scorpio.portfoliotracker.ui.data.settingsTab
import com.scorpio.portfoliotracker.ui.data.tabBarItems
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .statusBarsPadding()
                        .fillMaxSize()
                ) {
                    Scaffold(
                        bottomBar = { TabView(tabBarItems, navController) },
                    ) { innerPadding ->
                        NavHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            startDestination = portfolioTab.title
                        ) {
                            composable(portfolioTab.title) {
                                PortfolioScreen(viewModel = viewModel)
                            }
                            composable(homeTab.title) {
                                Text(text = "Home Screen")
                            }
                            composable(settingsTab.title) {
                                Text(text = "Settings Screen")
                            }
                        }
                    }
                }
            }
        }
    }
}

