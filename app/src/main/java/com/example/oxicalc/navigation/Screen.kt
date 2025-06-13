package com.example.oxicalc.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object History : Screen("history_screen")
    data object Rules : Screen("rules_screen")
    data object Onboarding : Screen("onboarding")

}
