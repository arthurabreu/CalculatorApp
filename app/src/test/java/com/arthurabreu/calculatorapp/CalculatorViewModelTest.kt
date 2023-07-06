package com.arthurabreu.calculatorapp

import com.arthurabreu.calculatorapp.viewmodel.CalculatorViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorViewModelTest {

    private lateinit var calculatorViewModel: CalculatorViewModel

    @Before
    fun setUp() {
        calculatorViewModel = CalculatorViewModel()
    }

    @Test
    fun `onAction sets state when action is Number`() {
        // Arrange
        val number = 5
        val expectedState = CalculatorState(number.toString(), "")

        // Act
        calculatorViewModel.onAction(CalculatorAction.Number(number))

        // Assert
        assertEquals(expectedState, calculatorViewModel.state)
    }

    @Test
    fun `onAction sets state when action is Decimal`() {
        // Arrange
        val initialNumber1 = "123"
        val expectedState = CalculatorState("$initialNumber1.", "", null)

        // Initialize calculatorViewModel to have state.number1 as "123"
        initialNumber1.forEach { digitChar ->
            val digit = Character.getNumericValue(digitChar)
            calculatorViewModel.onAction(CalculatorAction.Number(digit))
        }

        // Act
        calculatorViewModel.onAction(CalculatorAction.Decimal)

        // Assert
        assertEquals(expectedState, calculatorViewModel.state)
    }

    @Test
    fun `onAction sets state when action is Clear`() {
        calculatorViewModel.onAction(CalculatorAction.Clear)

        // Assert
        val expectedState = CalculatorState()
        assertEquals(expectedState, calculatorViewModel.state)
    }

    @Test
    fun `onAction sets state when action is Operation`() {
        // Arrange
        val initialNumber1 = "123"
        val operation = CalculatorOperation.Subtract
        val expectedState = CalculatorState(initialNumber1, "", operation)

        // Initialize calculatorViewModel to have state.number1 as "123"
        initialNumber1.forEach { digitChar ->
            val digit = Character.getNumericValue(digitChar)
            calculatorViewModel.onAction(CalculatorAction.Number(digit))
        }

        // Act
        calculatorViewModel.onAction(CalculatorAction.Operation(operation))

        // Assert
        assertEquals(expectedState, calculatorViewModel.state)
    }

    @Test
    fun `onAction calls performCalculation when action is Calculate`() {
        // Arrange
        val number1 = "10"
        val number2 = "5"
        val operation = CalculatorOperation.Add
        val expectedState = CalculatorState("15.0", "", null)

        // Initialize calculatorViewModel to have state.number1 as "10", state.number2 as "5", and state.operation as Add
        number1.forEach { digitChar ->
            val digit = Character.getNumericValue(digitChar)
            calculatorViewModel.onAction(CalculatorAction.Number(digit))
        }
        calculatorViewModel.onAction(CalculatorAction.Operation(operation))
        number2.forEach { digitChar ->
            val digit = Character.getNumericValue(digitChar)
            calculatorViewModel.onAction(CalculatorAction.Number(digit))
        }

        // Act
        calculatorViewModel.onAction(CalculatorAction.Calculate)

        // Assert
        assertEquals(expectedState, calculatorViewModel.state)
    }

    @Test
    fun `enterNumber does not add more digits if MAX_NUM_LENGTH is reached`() {
        // Arrange
        val initialNumber1 = "12345678" // 8 digits
        val extraNumber = 9
        val expectedState = CalculatorState(initialNumber1, "", null)

        // Initialize calculatorViewModel to have state.number1 as "12345678"
        initialNumber1.forEach { digitChar ->
            val digit = Character.getNumericValue(digitChar)
            calculatorViewModel.onAction(CalculatorAction.Number(digit))
        }

        // Act
        calculatorViewModel.onAction(CalculatorAction.Number(extraNumber))

        // Assert
        assertEquals(expectedState, calculatorViewModel.state)
    }

    @Test
    fun `performDeletion correctly deletes the last digit or operation`() {
        // Arrange
        val initialNumber1 = "123"
        val initialOperation = CalculatorOperation.Add
        val initialNumber2 = "456"
        val expectedState = CalculatorState("123", "45", initialOperation)

        // Initialize calculatorViewModel to have state.number1 as "123", state.operation as Add, and state.number2 as "456"
        initialNumber1.forEach { digitChar ->
            val digit = Character.getNumericValue(digitChar)
            calculatorViewModel.onAction(CalculatorAction.Number(digit))
        }
        calculatorViewModel.onAction(CalculatorAction.Operation(initialOperation))
        initialNumber2.forEach { digitChar ->
            val digit = Character.getNumericValue(digitChar)
            calculatorViewModel.onAction(CalculatorAction.Number(digit))
        }

        // Act
        calculatorViewModel.onAction(CalculatorAction.Delete)

        // Assert
        assertEquals(expectedState, calculatorViewModel.state)
    }

    @Test
    fun `performCalculation handles division by zero`() {
        // Arrange
        val number1 = "10"
        val number2 = "0"
        val operation = CalculatorOperation.Divide
        val expectedState = CalculatorState("Infinit", "", null)

        // Initialize calculatorViewModel to have state.number1 as "10", state.number2 as "0", and state.operation as Divide
        number1.forEach { digitChar ->
            val digit = Character.getNumericValue(digitChar)
            calculatorViewModel.onAction(CalculatorAction.Number(digit))
        }
        calculatorViewModel.onAction(CalculatorAction.Operation(operation))
        number2.forEach { digitChar ->
            val digit = Character.getNumericValue(digitChar)
            calculatorViewModel.onAction(CalculatorAction.Number(digit))
        }

        // Act
        calculatorViewModel.onAction(CalculatorAction.Calculate)

        // Assert
        assertEquals(expectedState, calculatorViewModel.state)
    }

    @Test
    fun `enterDecimal does not add a decimal if the number is blank`() {
        // Arrange
        val expectedState = CalculatorState()

        // Act
        calculatorViewModel.onAction(CalculatorAction.Decimal)

        // Assert
        assertEquals(expectedState, calculatorViewModel.state)
    }
}

