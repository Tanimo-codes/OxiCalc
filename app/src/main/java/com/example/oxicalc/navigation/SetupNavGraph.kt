package com.example.oxicalc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oxicalc.view.HomeScreen
import com.example.oxicalc.view.RulesScreen
import com.example.oxicalc.view.HistoryScreen
import com.example.oxicalc.viewModel.HistoryViewModel
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
            val oxidationViewModel: OxidationViewModel = hiltViewModel()
            val historyViewModel: HistoryViewModel = hiltViewModel()
            HomeScreen(viewModel = oxidationViewModel, historyViewModel = historyViewModel)
        }
        composable(route = Screen.History.route) {
            val viewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(viewModel = viewModel)
        }
        composable(route = Screen.Rules.route) {
            RulesScreen()
        }
    }
}