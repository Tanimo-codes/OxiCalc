package com.example.oxicalc.view

import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPage("Welcome to OxiCalc!", "Easily calculate oxidation states of elements in compounds and ions.", R.drawable.ic_chem_intro),
        OnboardingPage("How It Works", "Enter a formula and get step-by-step solutions.", R.drawable.ic_formula_step),
        OnboardingPage("Track and Revisit", "Your results are saved for future reference.", R.drawable.ic_history)
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
            TextButton(onClick = onFinished) {
                Text("Skip")
            }
            Button(
                onClick = {
                    if (pagerState.currentPage == pages.lastIndex) {
                        onFinished()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                }
            ) {
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
        modifier = Modifier.fillMaxSize()
    ) {
        Image(painterResource(id = page.imageRes), contentDescription = null)
        Spacer(modifier = Modifier.height(24.dp))
        Text(page.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(page.description, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)
