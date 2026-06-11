package com.example.insulator.core.data.ui

data class CalculatorParamsUiState(
    val glucose: String = "",
    val carbs: String = "",
    val foods: List<Food> = emptyList(),
    val ratio: String = "",
    val sensitivity: String = "",
    val targetGlucose: String = "",
    val dosis: Float = 0f,
    val dosisCorrection: Float = 0f,
    val foodDosis: Float = 0f,

    //FORM FIELDS VALIDATION
    val formValidationError : Boolean = true,
    val showGlucoseError : Boolean = false,
    val showCarbsError : Boolean = false,
    val showRatioError : Boolean = false,
    val showSensError : Boolean = false,
    val showTargetGlucoseError : Boolean = false
)