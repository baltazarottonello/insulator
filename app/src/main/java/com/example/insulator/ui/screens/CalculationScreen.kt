package com.example.insulator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulator.ui.components.ErrorBox
import com.example.insulator.ui.components.calculation.AutoCalculatedParams
import com.example.insulator.ui.components.calculation.BottomMessage
import com.example.insulator.ui.components.calculation.DosisCalculation
import com.example.insulator.ui.components.calculation.DosisResult
import com.example.insulator.ui.components.calculation.FoodParams
import com.example.insulator.ui.components.calculation.PatientSettingsCard
import com.example.insulator.ui.viewModels.calculation.InsulatorCalculationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalculationScreen(
    viewModel: InsulatorCalculationViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AutoCalculatedParams(
            glucose = uiState.glucose,
            showGlucoseError = uiState.showGlucoseError,
            carbs = uiState.carbs,
            showCarbsError = uiState.showCarbsError,
            onGlucoseChange = { viewModel.onGlucoseChanged(it) },
            onCarbsChange = { viewModel.onCarbsChanged(newValue = it) },
        )

        FoodParams(
            foodList = viewModel.availableFoods,
            onFoodClick = { food ->
                viewModel.addCarbsFromFood(food)
            },
            onClearClick = {
                viewModel.cleanAutoCalculatedParams()
            }
        )
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            if (uiState.getParamsError.isEmpty()) {
                PatientSettingsCard(
                    ratio = uiState.ratio,
                    showRatioError = uiState.showRatioError,
                    onRatioChange = { viewModel.onRatioChanged(it) },
                    sensitivity = uiState.sensitivity,
                    showSensError = uiState.showSensError,
                    onSensitivityChange = { viewModel.onSensitivityChanged(it) },
                    targetGlucose = uiState.targetGlucose,
                    showTargetGlucoseError = uiState.showTargetGlucoseError,
                    onTargetGlucoseChange = { viewModel.onTargetGlucoseChanged(it) },
                    enabled = !uiState.isPatient
                )
                DosisCalculation(handler = { viewModel.onCalculateClicked() })
            } else {
                ErrorBox(uiState.getParamsError)
            }
        }
        DosisResult(
            paramsError = uiState.getParamsError,
            dosis = uiState.dosis,
            correction = uiState.dosisCorrection,
            food = uiState.foodDosis,
            formValidationError = uiState.formValidationError
        )

        BottomMessage()

    }
}