package com.schwarckdev.cerofiao.core.designsystem.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Stack

/**
 * Utilidad para evaluar expresiones matemáticas simples como "50+15*2".
 * Utiliza el algoritmo Shunting-yard modificado para evaluación directa con BigDecimal, 
 * cumpliendo la regla crítica financiera de la app.
 */
object ExpressionParser {

    fun evaluate(expression: String): BigDecimal {
        if (expression.isBlank()) return BigDecimal.ZERO

        val tokens = tokenize(expression.replace(" ", ""))
        if (tokens.isEmpty()) return BigDecimal.ZERO

        return try {
            evaluatePostfix(infixToPostfix(tokens)).setScale(2, RoundingMode.HALF_EVEN)
        } catch (e: Exception) {
            // En caso de fallo sintáctico (ej. "50+"), retornamos 0 o el último número válido.
            BigDecimal.ZERO
        }
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var currentNumber = StringBuilder()

        for (i in expr.indices) {
            val char = expr[i]
            if (char.isDigit() || char == '.') {
                currentNumber.append(char)
            } else if (isOperator(char)) {
                if (currentNumber.isNotEmpty()) {
                    tokens.add(currentNumber.toString())
                    currentNumber.clear()
                }
                tokens.add(char.toString())
            }
        }
        if (currentNumber.isNotEmpty()) {
            tokens.add(currentNumber.toString())
        }
        return tokens
    }

    private fun isOperator(c: Char) = c == '+' || c == '-' || c == '*' || c == '/'

    private fun precedence(op: String): Int {
        return when (op) {
            "+", "-" -> 1
            "*", "/" -> 2
            else -> -1
        }
    }

    private fun infixToPostfix(infix: List<String>): List<String> {
        val output = mutableListOf<String>()
        val operators = Stack<String>()

        for (token in infix) {
            if (token.matches(Regex("[0-9.]+"))) {
                output.add(token)
            } else if (token.length == 1 && isOperator(token[0])) {
                while (operators.isNotEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    output.add(operators.pop())
                }
                operators.push(token)
            }
        }

        while (operators.isNotEmpty()) {
            output.add(operators.pop())
        }

        return output
    }

    private fun evaluatePostfix(postfix: List<String>): BigDecimal {
        val stack = Stack<BigDecimal>()

        for (token in postfix) {
            if (token.matches(Regex("[0-9.]+"))) {
                stack.push(BigDecimal(token))
            } else if (token.length == 1 && isOperator(token[0])) {
                if (stack.size < 2) continue // Expresion incompleta temporalmente
                val b = stack.pop()
                val a = stack.pop()
                val result = when (token) {
                    "+" -> a.add(b)
                    "-" -> a.subtract(b)
                    "*" -> a.multiply(b)
                    "/" -> {
                        if (b.compareTo(BigDecimal.ZERO) == 0) BigDecimal.ZERO 
                        else a.divide(b, 4, RoundingMode.HALF_EVEN)
                    }
                    else -> BigDecimal.ZERO
                }
                stack.push(result)
            }
        }

        return if (stack.isNotEmpty()) stack.pop() else BigDecimal.ZERO
    }
}
