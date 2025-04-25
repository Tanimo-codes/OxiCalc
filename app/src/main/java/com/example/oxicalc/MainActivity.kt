package com.example.oxicalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.oxicalc.ui.theme.OxiCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            OxiCalcTheme {
                var selectedItem by remember { mutableIntStateOf(0) }
                val items = listOf("Home", "History", "Rules")
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
                                    onClick = { selectedItem = index }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
            }
        }



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var formula by remember { mutableStateOf("") }

        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = formula,
            onValueChange = { formula = it },
            label = { Text("Enter formula") }
        )
        Spacer(modifier = Modifier.height(30.dp))

        ElevatedButton(onClick = { }) {
            Text("Solve",
                fontSize = 22.sp,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OxiCalcTheme {
        Greeting("Android")
    }
}