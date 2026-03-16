package com.schwarckdev.cerofiao.core.common

/**
 * Simple math expression evaluator for the Numpad calculator.
 * Supports +, -, *, / with standard operator precedence.
 */
object ExpressionEvaluator {

    fun evaluate(expression: String): Double? {
        return try {
            val sanitized = expression
                .replace(" ", "")
                .replace("×", "*")
                .replace("÷", "/")
                .replace(",", ".")
            parseExpression(sanitized, 0).first
        } catch (_: Exception) {
            null
        }
    }

    private fun parseExpression(expr: String, pos: Int): Pair<Double, Int> {
        var (left, currentPos) = parseTerm(expr, pos)

        while (currentPos < expr.length) {
            val op = expr[currentPos]
            if (op != '+' && op != '-') break
            val (right, nextPos) = parseTerm(expr, currentPos + 1)
            left = if (op == '+') left + right else left - right
            currentPos = nextPos
        }

        return left to currentPos
    }

    private fun parseTerm(expr: String, pos: Int): Pair<Double, Int> {
        var (left, currentPos) = parseFactor(expr, pos)

        while (currentPos < expr.length) {
            val op = expr[currentPos]
            if (op != '*' && op != '/') break
            val (right, nextPos) = parseFactor(expr, currentPos + 1)
            left = if (op == '*') left * right else {
                if (right == 0.0) return Double.NaN to nextPos
                left / right
            }
            currentPos = nextPos
        }

        return left to currentPos
    }

    private fun parseFactor(expr: String, pos: Int): Pair<Double, Int> {
        var currentPos = pos

        // Handle negative sign
        val negative = currentPos < expr.length && expr[currentPos] == '-'
        if (negative) currentPos++

        // Handle parentheses
        if (currentPos < expr.length && expr[currentPos] == '(') {
            val (value, nextPos) = parseExpression(expr, currentPos + 1)
            // skip closing ')'
            val afterParen = if (nextPos < expr.length && expr[nextPos] == ')') nextPos + 1 else nextPos
            return (if (negative) -value else value) to afterParen
        }

        // Parse number
        val start = currentPos
        while (currentPos < expr.length && (expr[currentPos].isDigit() || expr[currentPos] == '.')) {
            currentPos++
        }
        val numberStr = expr.substring(start, currentPos)
        val value = numberStr.toDoubleOrNull() ?: 0.0
        return (if (negative) -value else value) to currentPos
    }
}
