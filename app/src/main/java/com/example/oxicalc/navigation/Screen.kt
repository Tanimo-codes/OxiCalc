package com.example.oxicalc.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object History : Screen("history_screen")
    object Rules : Screen("rules_screen")

}
