package com.example.oxicalc.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RulesScreen() {

    Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Rules for Using OxiCalc",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "• Enter a valid chemical formula in the \"Enter formula\" field (e.g., Fe2O3).",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "• Specify the target element whose oxidation state you want to calculate (e.g., Fe).",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "• When you press Solve, the app will:\n" +
                        "  - Parse your formula and display a cleaned-up version.\n" +
                        "  - Calculate and display the oxidation state of your target element.\n" +
                        "  - Save the calculation in your history for easy reference.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "• Results format:\n  The oxidation state will be shown like this:\n  Fe = +3\n  where the element symbol is followed by an equal sign and its calculated oxidation state.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "• Error Handling:\n  If the target element is not found in the formula, or the formula is invalid, an error message will guide you.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
