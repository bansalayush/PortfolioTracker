package com.scorpio.portfoliotracker.ui.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

val homeTab = TabBarItem(
    title = "Home",
    selectedIcon = Icons.Filled.Home,
    unselectedIcon = Icons.Outlined.Home
)
val portfolioTab = TabBarItem(
    title = "Portfolio",
    selectedIcon = Icons.Filled.List,
    unselectedIcon = Icons.Outlined.List
)
val settingsTab = TabBarItem(
    title = "Settings",
    selectedIcon = Icons.Filled.Settings,
    unselectedIcon = Icons.Outlined.Settings
)

// creating a list of all the tabs
val tabBarItems = listOf(homeTab, portfolioTab, settingsTab)
