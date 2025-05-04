package com.example.oxicalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.oxicalc.navigation.Screen
import com.example.oxicalc.navigation.SetupNavGraph
import com.example.oxicalc.ui.theme.OxiCalcTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            OxiCalcTheme {
                val navController = rememberNavController()
                var selectedItem by remember { mutableIntStateOf(0) }
                val items = listOf("Home", "History", "Rules")
                val routes = listOf(Screen.Home.route, Screen.History.route, Screen.Rules.route)
                val selectedIcons = listOf(
                    Icons.Filled.Home,
                    Icons.Filled.History,
                    Icons.Filled.Info
                )
                val unselectedIcons = listOf(
                    Icons.Outlined.Home,
                    Icons.Outlined.History,
                    Icons.Outlined.Info
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                            contentDescription = item
                                        )
                                    },
                                    label = { Text(item) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        navController.navigate(routes[index]) {
                                            // Pop up to start destination and clear back stack
                                            popUpTo(Screen.Home.route) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    // This replaces your direct call to Greeting
                    SetupNavGraph(
                        navController =navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}