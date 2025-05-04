package com.example.oxicalc.viewModel.parser

sealed class FormulaPart

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


object FormulaParser {

    fun parse(input: String): ParsedFormula {
        val cleanedInput = input.replace("\\s".toRegex(), "")
        val charge = extractCharge(cleanedInput)
        val formulaBody = cleanedInput.removeSuffixCharge()
        val parts = parseFormulaBody(formulaBody)
        return ParsedFormula(parts, charge)
    }

    private fun extractCharge(input: String): Int {
        val chargeRegex = Regex("""\^?([+-]?\d*)([+-])$""")
        val match = chargeRegex.find(input)
        return if (match != null) {
            val (numStr, sign) = match.destructured
            val magnitude = if (numStr.isBlank()) 1 else numStr.toInt()
            if (sign == "+") magnitude else -magnitude
        } else 0
    }

    private fun String.removeSuffixCharge(): String {
        return this.replace(Regex("""\^?[+-]?\d*[+-]$"""), "")
    }

    private fun parseFormulaBody(input: String): List<FormulaPart> {
        val stack = mutableListOf<MutableList<FormulaPart>>()
        var i = 0
        val currentList = mutableListOf<FormulaPart>()
        stack.add(currentList)

        while (i < input.length) {
            when (val c = input[i]) {
                '(' -> {
                    val newGroup = mutableListOf<FormulaPart>()
                    stack.add(newGroup)
                    i++
                }
                ')' -> {
                    val completedGroup = stack.removeLast()
                    i++
                    val (multiplier, step) = readNumber(input, i)
                    i += step
                    val groupPart = GroupPart(completedGroup, multiplier)
                    stack.last().add(groupPart)
                }
                in 'A'..'Z' -> {
                    val (symbol, nextIndex) = readElementSymbol(input, i)
                    i = nextIndex
                    val (count, step) = readNumber(input, i)
                    i += step
                    stack.last().add(ElementPart(symbol, count))
                }
                else -> i++ // Skip unexpected characters
            }
        }

        return stack.first()
    }

    private fun readElementSymbol(input: String, start: Int): Pair<String, Int> {
        var i = start + 1
        while (i < input.length && input[i].isLowerCase()) i++
        return input.substring(start, i) to i
    }

    private fun readNumber(input: String, start: Int): Pair<Int, Int> {
        if (start >= input.length || !input[start].isDigit()) return 1 to 0
        var i = start
        while (i < input.length && input[i].isDigit()) i++
        return input.substring(start, i).toInt() to (i - start)
    }
}

fun formatParsedFormula(formula: ParsedFormula): String {
    val builder = StringBuilder()

    for (part in formula.parts) {
        builder.append(formatPart(part))
    }

    if (formula.charge != 0) {
        builder.append(formula.charge.toSuperscript())
    }

    return builder.toString()
}

fun formatPart(part: FormulaPart): String = when (part) {
    is ElementPart -> part.symbol + (if (part.count > 1) part.count.toSubscript() else "")
    is GroupPart -> {
        val inner = part.parts.joinToString("") { formatPart(it) }
        "($inner)" + (if (part.multiplier > 1) part.multiplier.toSubscript() else "")
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
    this.toString().map { subscriptDigits[it] ?: it }.joinToString("")

fun Int.toSuperscript(): String {
    val absValue = kotlin.math.abs(this).toString()
    val sign = if (this > 0) "+" else if (this < 0) "-" else ""
    return absValue.map { superscriptDigits[it] ?: it }.joinToString("") +
            (if (sign.isNotEmpty()) superscriptDigits[sign[0]] else "")
}

