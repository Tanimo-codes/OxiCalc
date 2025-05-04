package com.example.oxicalc.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oxicalc.viewModel.OxidationViewModel


@Composable
fun HomeScreen(viewModel: OxidationViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(text = "OxiCalc")

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = viewModel.formulaInput,
            onValueChange = { viewModel.formulaInput = it },
            label = { Text("Enter formula") }
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = viewModel.targetElement,
            onValueChange = { viewModel.targetElement = it },
            label = { Text("Target element (e.g., Fe)") },

        )

        Spacer(modifier = Modifier.height(30.dp))

        ElevatedButton(
            onClick = { viewModel.onCalculateClicked() }
        ) {
            Text("Solve",
                fontSize = 22.sp,
                color = Color.DarkGray
            )
        }

        Spacer(Modifier.height(16.dp))

        if (viewModel.formattedDisplay.isNotBlank()) {
            Text("Parsed Formula:", fontWeight = FontWeight.Bold)
            Text(viewModel.formattedDisplay, fontSize = 20.sp)
            Spacer(Modifier.height(8.dp))
        }

        viewModel.oxidationResult?.let {
            Text("Oxidation State of ${it.element}: ${it.value}", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Reasoning:")
            Text(it.reasoning, fontStyle = FontStyle.Italic)
        }

        if (viewModel.errorMessage.isNotBlank()) {
            Text(viewModel.errorMessage, color = Color.Red)
        }
    }
}

// TODO: Create light and dark mode themes and beautify UI instead of device default themes
// TODO: Create a room db for storing the history
// TODO: resolve the calculation algorithm its a bit wonky
// TODO: add other parts into the dictionary that the app will use (eg. metal complexes)
// TODO: Learn and perform unit tests
// todo: Add that intro feature you see while using new apps
// TODO: If everything works fine, add Ads to the app, small ones

@Preview (showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

