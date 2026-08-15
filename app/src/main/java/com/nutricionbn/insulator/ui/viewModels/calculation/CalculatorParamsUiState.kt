package com.nutricionbn.insulator.ui.viewModels.calculation

import com.nutricionbn.insulator.core.data.repositories.calculation.DoseCalculationSnapshot

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
    val isLoading: Boolean = false,
    val isPatient: Boolean = false,
    val showDoseResultCard: Boolean = false,

    //GENERAL
    val error: String = "",

    //CONFIRMATION FLOW
    val showConfirmationButton: Boolean = false,
    val showUnsavedWarningDialog: Boolean = false,
    val showPendingSnapshotConfirmationDialog: Boolean = false,
    val loadingConfirmation: Boolean = false,
    val loadingConfirmationSuccess: Boolean = false,
    val loadingConfirmationError: String = "",
    val lastDoseCalculationSnapshot: DoseCalculationSnapshot?,
    val loadingPendingSnapshot: Boolean = false,
    val showDoseCalculationConfirmationDialog: Boolean = false,

    //FORM FIELDS VALIDATION
    val formValidationError: Boolean = false,
    val showGlucoseError: Boolean = false,
    val showCarbsError: Boolean = false,
    val showRatioError: Boolean = false,
    val showSensError: Boolean = false,
    val showTargetGlucoseError: Boolean = false,
    val glucoseOutOfRange: Boolean = false,
    val carbsOutOfRange: Boolean = false,
    val ratioOutOfRange: Boolean = false,
    val sensOutOfRange: Boolean = false,
    val targetGlucoseOutOfRange: Boolean = false,
    val getParamsError: String = ""
)