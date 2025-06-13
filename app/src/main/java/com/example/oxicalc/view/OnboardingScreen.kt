package com.example.oxicalc.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.oxicalc.R
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.oxicalc.model.saveOnboardingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first

// Extension for datastore
private val Context.dataStore by preferencesDataStore(name = "settings")
private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_completed")

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val pages = listOf(
        OnboardingPage(
            "Welcome to OxiCalc!",
            "Easily calculate oxidation states of elements in compounds and ions.",
            R.drawable.ic_chem_intro
        ),
        OnboardingPage(
            "How It Works",
            "Enter a formula and get step-by-step solutions.",
            R.drawable.ic_formula_step
        ),
        OnboardingPage(
            "Track and Revisit",
            "Your results are saved for future reference.",
            R.drawable.ic_history
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HorizontalPager(count = pages.size, state = pagerState) { page ->
            OnboardingPageContent(page = pages[page])
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {
                scope.launch {
                    saveOnboardingState(context, true)
                    onFinished()
                }
            }) {
                Text("Skip")
            }

            Button(onClick = {
                if (pagerState.currentPage == pages.lastIndex) {
                    scope.launch {
                        saveOnboardingState(context, true)
                        onFinished()
                    }
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }) {
                Text(if (pagerState.currentPage == pages.lastIndex) "Start" else "Next")
            }
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(painter = painterResource(id = page.imageRes), contentDescription = null)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = page.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}




