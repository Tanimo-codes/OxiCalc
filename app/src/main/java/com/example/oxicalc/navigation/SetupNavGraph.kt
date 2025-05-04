package com.example.oxicalc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oxicalc.View.HomeScreen
import com.example.oxicalc.View.RulesScreen
import com.example.oxicalc.View.HistoryScreen
import com.example.oxicalc.viewModel.OxidationViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            val viewModel: OxidationViewModel = hiltViewModel()
            HomeScreen(viewModel = viewModel)
        }
        composable(route = Screen.History.route) {
            HistoryScreen()
        }
        composable(route = Screen.Rules.route) {
            RulesScreen()
        }
    }
}