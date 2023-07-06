package com.arthurabreu.calculatorapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.arthurabreu.calculatorapp.CalculatorAction
import com.arthurabreu.calculatorapp.CalculatorState
import com.arthurabreu.calculatorapp.CalculatorOperation

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Delete -> performDeletion()
        }
    }

    private fun performDeletion() {
        when {
            state.number2.isNotBlank() -> state = state.copy(number2 = state.number2.dropLast(1))
            state.operation != null -> state = state.copy(operation = null)
            state.number1.isNotBlank() -> state = state.copy(number1 = state.number1.dropLast(1))
        }
    }

    private fun performCalculation() {
        val n1 = state.number1.toDoubleOrNull()
        val n2 = state.number2.toDoubleOrNull()
        if (n1 != null && n2 != null) {
            val result = when(state.operation) {
                is CalculatorOperation.Add -> n1.plus(n2)
                is CalculatorOperation.Subtract -> n1.minus(n2)
                is CalculatorOperation.Multiply -> n1.times(n2)
                is CalculatorOperation.Divide -> n1.div(n2)
                null -> return
            }
            // Take only the first 15 characters of result to not make result number too long
            state = state.copy(
                number1 = result.toString().take(7),
                number2 = "",
                operation = null
            )
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        // If n1 isn't blank we can add an operation to the string
        if (state.number1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        // If we didn't enter an operation and n1 isn't blank or with a decimal we can add a decimal
        if (
            state.operation == null &&
            !state.number1.contains(".") &&
            state.number1.isNotBlank()
        ) {
            state = state.copy(number1 = state.number1 + ".")
            return
        }
        // If n2 doesn't contain a decimal and is not blank we can add a decimal.
        // Operation at this point won't be null so no need to check it
        if (!state.number2.contains(".") && state.number2.isNotBlank()) {
            state = state.copy(number2 = state.number2 + ".")
        }
    }

    private fun enterNumber(number: Int) {
        // If we didn't enter an operation yet and number isn't too long, add it to the string
        if (state.operation == null) {
            if (state.number1.length >= MAX_NUM_LENGTH) {
                return
            }
            state = state.copy(number1 = state.number1 + number)
            return
        }
        // Enter second number
        if (state.number2.length >= MAX_NUM_LENGTH) {
            return
        }
        state = state.copy(number2 = state.number2 + number)
    }

    companion object {
        private const val MAX_NUM_LENGTH = 8
    }
}