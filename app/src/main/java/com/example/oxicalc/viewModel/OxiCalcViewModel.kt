package com.example.oxicalc.viewModel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.oxicalc.model.CalculationHistoryItem
import com.example.oxicalc.viewModel.parser.FormulaParser
import com.example.oxicalc.viewModel.parser.OxidationStateCalculator
import com.example.oxicalc.viewModel.parser.OxidationStateResult
import com.example.oxicalc.viewModel.parser.formatParsedFormula
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OxidationViewModel @Inject constructor(): ViewModel() {
    var formulaInput by mutableStateOf("")
    var targetElement by mutableStateOf("")

    var formattedDisplay by mutableStateOf("")
    var oxidationResult by mutableStateOf<OxidationStateResult?>(null)
    var errorMessage by mutableStateOf("")

    fun onCalculateClicked(onSuccess: (CalculationHistoryItem) -> Unit = {}) { // Add onSuccess parameter
        try {
            val parsed = FormulaParser.parse(formulaInput)
            formattedDisplay = formatParsedFormula(parsed)
            val result = OxidationStateCalculator.calculate(parsed, targetElement)
            if (result != null) { // Check if result is not null
                oxidationResult = result
                errorMessage = ""

                // Trigger history save
                val historyItem = CalculationHistoryItem(
                    formula = formulaInput,
                    targetElement = result.element,
                    solution = result.reasoning,
                    oxidationState = result.value
                )
                onSuccess(historyItem)

            } else {
                oxidationResult = null
                errorMessage = "Element $targetElement not found in formula."
            }
        } catch (e: Exception) {
            errorMessage = "Parsing error: ${e.message}"
        }
    }
}
