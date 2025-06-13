package com.example.oxicalc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oxicalc.view.HomeScreen
import com.example.oxicalc.view.HistoryScreen
import com.example.oxicalc.view.OnboardingScreen
import com.example.oxicalc.view.RulesScreen
import com.example.oxicalc.viewModel.HistoryViewModel
import com.example.oxicalc.viewModel.OxidationViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route // ← This allows dynamic start screen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(onFinished = {
                // Save onboarding state and navigate
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            })
        }
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
