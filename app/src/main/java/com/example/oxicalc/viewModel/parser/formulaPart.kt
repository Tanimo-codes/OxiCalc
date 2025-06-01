package com.example.oxicalc.viewModel.parser

sealed class FormulaPart // Sealed class to represent the parts of the formula

data class ElementPart(
    val symbol: String,
    val count: Int = 1
) : FormulaPart()

data class GroupPart(
    val parts: List<FormulaPart>,
    val multiplier: Int = 1
) : FormulaPart()

data class ParsedFormula(
    val parts: List<FormulaPart>,
    val charge: Int = 0
)

// Parser for the formula input, will convert string to Parsed formula object
object FormulaParser {

    fun parse(input: String): ParsedFormula {
        val cleanedInput = input.replace("\\s".toRegex(), "") // Convert to regex and Remove spaces
        val charge = extractCharge(cleanedInput)
        val formulaBody = cleanedInput.removeSuffixCharge()
        val parts = parseFormulaBody(formulaBody)
        return ParsedFormula(parts, charge)
    }

    private fun extractCharge(input: String): Int {
        val chargeRegex = Regex("""\^?([+-]?\d*)([+-])$""") // identify the charge
        val match = chargeRegex.find(input)
        return if (match != null) {
            val (numStr, sign) = match.destructured
            val magnitude = if (numStr.isBlank()) 1 else numStr.toInt() // if the charge is just a sign  - or + default value is 1
            if (sign == "+") magnitude else -magnitude // return the charge if positive else return negative
        } else 0 // nothing is found
    }

    private fun String.removeSuffixCharge(): String {
        return this.replace(Regex("""\^?[+-]?\d*[+-]$"""), "") // remove the charge from the string
    }

    private fun parseFormulaBody(input: String): List<FormulaPart> {
        val stack = mutableListOf<MutableList<FormulaPart>>() // Create a stack to hold the groups
        var i = 0  // initialize position
        val currentList = mutableListOf<FormulaPart>()
        stack.add(currentList)

        // Main parsing loop
        while (i < input.length) {
            when (val c = input[i]) {
                '(' -> {
                    val newGroup = mutableListOf<FormulaPart>() // Create a new group when a parentheses '(' is found
                    stack.add(newGroup)
                    i++
                }
                ')' -> {
                    val completedGroup = stack.removeLast() // Remove or pop the last group when a parentheses ')' is found
                    i++
                    val (multiplier, step) = readNumber(input, i) // Check for a multiplier, or a digit string
                    i += step
                    val groupPart = GroupPart(completedGroup, multiplier)
                    stack.last().add(groupPart) // Add the  functional group to the previous list from the stack
                }
                in 'A'..'Z' -> {
                    var matched = false

                    // Try to match known radicals (e.g., SO4, NO3) from the current position
                    for ((radical, _) in OxidationStateCalculator.knownRadicals) {
                        if (input.startsWith(radical, i)) {
                            i += radical.length // Move the index to the end of the known radical
                            val (count, step) = readNumber(input, i)
                            i += step
                            stack.last().add(ElementPart(radical, count)) // Add the known radical to the current list
                            matched = true
                            break
                        }
                    }

                    if (!matched) { // If no known radical is matched, read the element symbol and count
                        val (symbol, nextIndex) = readElementSymbol(input, i)
                        i = nextIndex
                        val (count, step) = readNumber(input, i)
                        i += step
                        stack.last().add(ElementPart(symbol, count))
                    }
                }


                else -> i++ // Skip unexpected characters
            }
        }

        return stack.first().map { part ->
            if (part is GroupPart) {
                val radicalKey = groupAsRadicalKey(part.parts)
                if (radicalKey != null && OxidationStateCalculator.knownRadicals.containsKey(radicalKey)) {
                    ElementPart(radicalKey, part.multiplier) // convert group to known radical
                } else {
                    part
                }
            } else part
        }
    }

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



// && AND, || OR and ! NOT
    private fun readElementSymbol(input: String, start: Int): Pair<String, Int> {
        var i = start + 1
        while (i < input.length && input[i].isLowerCase()) i++ // Read the element symbol
        return input.substring(start, i) to i // Return the element symbol and the index after it
    }

    private fun readNumber(input: String, start: Int): Pair<Int, Int> {
        if (start >= input.length || !input[start].isDigit()) return 1 to 0 // If no digit is found, return 1, since elements without an explicit count have an implicit count of 1
        var i = start
        while (i < input.length && input[i].isDigit()) i++ // Read the digit string
        return input.substring(start, i).toInt() to (i - start) // Return the digit string and parse to Integer, and the index after it
    }
}

fun formatParsedFormula(formula: ParsedFormula): String {
    val builder = StringBuilder()

    for (part in formula.parts) { // Iterate through the parts of the formula
        builder.append(formatPart(part))
    }

    if (formula.charge != 0) {
        builder.append(formula.charge.toSuperscript()) // Convert the charge to superscript
    }

    return builder.toString()
}

fun formatPart(part: FormulaPart): String = when (part) {
    is ElementPart -> part.symbol + (if (part.count > 1) part.count.toSubscript() else "") // check for element and convert the multiplier to a subscript if greater than 1
    is GroupPart -> {
        val inner = part.parts.joinToString("") { formatPart(it) } // Join the parts of the group
        "($inner)" + (if (part.multiplier > 1) part.multiplier.toSubscript() else "") // surrounds it in parenthesis and convert the multiplier to a subscript
    }
}

private val subscriptDigits = mapOf(
    '0' to '₀', '1' to '₁', '2' to '₂', '3' to '₃',
    '4' to '₄', '5' to '₅', '6' to '₆', '7' to '₇',
    '8' to '₈', '9' to '₉'
)

private val superscriptDigits = mapOf(
    '0' to '⁰', '1' to '¹', '2' to '²', '3' to '³',
    '4' to '⁴', '5' to '⁵', '6' to '⁶', '7' to '⁷',
    '8' to '⁸', '9' to '⁹', '+' to '⁺', '-' to '⁻'
)

fun Int.toSubscript(): String =
    this.toString().map { subscriptDigits[it] ?: it }.joinToString("") // matching and conversion for subscripts based on mapping above

fun Int.toSuperscript(): String {
    val absValue = kotlin.math.abs(this).toString()
    val sign = if (this > 0) "+" else if (this < 0) "-" else "" // check for sign and convert to superscript
    return absValue.map { superscriptDigits[it] ?: it }.joinToString("") +
            (if (sign.isNotEmpty()) superscriptDigits[sign[0]] else "")
}


