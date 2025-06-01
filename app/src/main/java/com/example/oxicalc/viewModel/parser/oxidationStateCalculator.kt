package com.example.oxicalc.viewModel.parser

data class OxidationStateResult(
    val element: String,
    val value: Int,
    val reasoning: String
)

object OxidationStateCalculator {

    // Known oxidation states for elements
    private val knownStates = mapOf(
        "O" to -2,

        // Group 0 (halogens)
        "F" to -1,
        "Cl" to -1,
        "Br" to -1,
        "I" to -1,
        // Group 1 (alkali) metals
        "H" to +1,
        "Na" to +1,
        "Li" to +1,
        "Rb" to +1,
        "K" to +1,
        "Cs" to +1,
        "Fr" to +1,
        // Group 2 (alkaline earth metals)
        "Be" to +2,
        "Ca" to +2,
        "Mg" to +2,
        "Sr" to +2,
        "Ba" to +2,
        "Ra" to +2,
        // Group 3 (transition metals)
        "Al" to +3,
        // groups like en, OH etc

    )

    val knownRadicals = mapOf(
        "SO4" to -2,
        "NO3" to -1,
        "CO3" to -2,
        "PO4" to -3,
        "OH" to -1,
        "NH4" to +1,
        "CN" to -1,
        "en"  to 0,

        )

    fun calculate(formula: ParsedFormula, target: String): OxidationStateResult? {
        val elementCounts = mutableMapOf<String, Int>()
        flattenFormula(formula.parts, 1, elementCounts)

        val totalCharge = formula.charge

        var knownSum = 0
        var unknownCount = 0

        for ((symbol, count) in elementCounts) {
            if (symbol == target) {
                unknownCount += count
            } else if (knownRadicals.containsKey(symbol)) {
                knownSum += knownRadicals[symbol]!! * count
            } else if (knownStates.containsKey(symbol)) {
                knownSum += knownStates[symbol]!! * count
            } else {
                // Optionally log or skip
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
                    result[part.symbol] =
                        result.getOrDefault(part.symbol, 0) + part.count * multiplier
                }

                is GroupPart -> {
                    val maybeRadical = groupAsRadicalKey(part.parts)
                    if (maybeRadical != null && knownRadicals.containsKey(maybeRadical)) { // Check for known radicals
                        result[maybeRadical] =
                            result.getOrDefault(maybeRadical, 0) + multiplier * part.multiplier
                    } else {
                        flattenFormula(part.parts, multiplier * part.multiplier, result)
                    }
                }
            }
        }
    }

    // Converts a group of ElementParts into a radical string like "SO4" or "NO3"
    private fun groupAsRadicalKey(parts: List<FormulaPart>): String? {
        return buildString {
            for (part in parts) {
                if (part is ElementPart) {
                    append(part.symbol)
                    if (part.count > 1) append(part.count)
                } else {
                    return null // nested groups not considered radicals here
                }
            }
        }
    }
}


  /*  fun calculate(formula: ParsedFormula, target: String): OxidationStateResult? {
        val elementCounts = mutableMapOf<String, Int>()
        flattenFormula(formula.parts, 1, elementCounts)

        val totalCharge = formula.charge

        var knownSum = 0
        var unknownCount = 0

        for ((element, count) in elementCounts) { // Iterate through the elements and their counts
            if (element == target) {
                unknownCount += count // Count the target element
                continue
            }

            val knownOx = knownStates[element]
            if (knownOx != null) { // Check if the element is known
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
            reasoning = "$unknownCount × $target + known sum ($knownSum) = charge ($totalCharge)" // Explain the reasoning
        )
    }

    private fun flattenFormula( // Flatten the formula
        parts: List<FormulaPart>,
        multiplier: Int,
        result: MutableMap<String, Int>
    ) {
        for (part in parts) {
            when (part) {  // Handle different parts of the formula
                is ElementPart -> {
                    result[part.symbol] = result.getOrDefault(part.symbol, 0) + part.count * multiplier
                }
                is GroupPart -> {
                    flattenFormula(part.parts, multiplier * part.multiplier, result)
                }
            }
        }
    } */

