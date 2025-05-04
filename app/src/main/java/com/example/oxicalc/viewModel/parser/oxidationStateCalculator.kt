package com.example.oxicalc.viewModel.parser

data class OxidationStateResult(
    val element: String,
    val value: Int,
    val reasoning: String
)

object OxidationStateCalculator {

    private val knownStates = mapOf(
        "O" to -2,
        "H" to +1,
        "F" to -1,
        "Cl" to -1,
        "Br" to -1,
        "I" to -1,
        "Na" to +1,
        "K" to +1,
        "Ca" to +2,
        "Mg" to +2,
        "Al" to +3,
    )

    fun calculate(formula: ParsedFormula, target: String): OxidationStateResult? {
        val elementCounts = mutableMapOf<String, Int>()
        flattenFormula(formula.parts, 1, elementCounts)

        val totalCharge = formula.charge

        var knownSum = 0
        var unknownCount = 0

        for ((element, count) in elementCounts) {
            if (element == target) {
                unknownCount += count
                continue
            }

            val knownOx = knownStates[element]
            if (knownOx != null) {
                knownSum += knownOx * count
            } else {
                // Skip unknowns unless we handle multiple unknowns
            }
        }

        if (unknownCount == 0) return null

        val targetOx = (totalCharge - knownSum) / unknownCount

        return OxidationStateResult(
            element = target,
            value = targetOx,
            reasoning = "$unknownCount × $target + known sum ($knownSum) = charge ($totalCharge)"
        )
    }

    private fun flattenFormula(
        parts: List<FormulaPart>,
        multiplier: Int,
        result: MutableMap<String, Int>
    ) {
        for (part in parts) {
            when (part) {
                is ElementPart -> {
                    result[part.symbol] = result.getOrDefault(part.symbol, 0) + part.count * multiplier
                }
                is GroupPart -> {
                    flattenFormula(part.parts, multiplier * part.multiplier, result)
                }
            }
        }
    }
}
