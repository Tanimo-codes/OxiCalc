package com.example.oxicalc.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oxicalc.ui.theme.OxiCalcTheme
import com.example.oxicalc.viewModel.HistoryViewModel
import com.example.oxicalc.viewModel.OxidationViewModel

@Composable
fun HomeScreen(
    viewModel: OxidationViewModel = viewModel(),
    historyViewModel: HistoryViewModel = viewModel()
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        OutlinedTextField(
            value = viewModel.formulaInput,
            onValueChange = { viewModel.formulaInput = it },
            label = { Text("Enter formula") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.secondary,
                cursorColor = colorScheme.primary
            )
        )

        OutlinedTextField(
            value = viewModel.targetElement,
            onValueChange = { viewModel.targetElement = it },
            label = { Text("Target element (e.g., Fe)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.secondary,
                cursorColor = colorScheme.primary
            )
        )

        ElevatedButton(
            onClick = {
                viewModel.onCalculateClicked { item ->
                    historyViewModel.addHistory(item)
                }
            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = colorScheme.primaryContainer,
                contentColor = colorScheme.onPrimaryContainer
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Solve", fontSize = 20.sp)
        }

        if (viewModel.formattedDisplay.isNotBlank()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Parsed Formula:", fontWeight = FontWeight.Bold)
                    Text(viewModel.formattedDisplay, fontSize = 18.sp)
                }
            }
        }

        viewModel.oxidationResult?.let {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Oxidation State of ${it.element}: ${it.value}", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Reasoning:", fontWeight = FontWeight.Medium)
                    Text(it.reasoning, fontStyle = FontStyle.Italic)
                }
            }
        }

        if (viewModel.errorMessage.isNotBlank()) {
            Text(viewModel.errorMessage, color = colorScheme.error)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Wrap in your app theme
    OxiCalcTheme {
        HomeScreen()
    }
}


/*

todo: Add that intro feature you see while using new apps
*/

