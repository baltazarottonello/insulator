package com.nutricionbn.insulator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutricionbn.insulator.ui.components.DoseConfirmationDialog
import com.nutricionbn.insulator.ui.components.ErrorBox
import com.nutricionbn.insulator.ui.components.calculation.AutoCalculatedParams
import com.nutricionbn.insulator.ui.components.calculation.BottomMessage
import com.nutricionbn.insulator.ui.components.calculation.DoseCalculation
import com.nutricionbn.insulator.ui.components.calculation.DosisResult
import com.nutricionbn.insulator.ui.components.calculation.FoodParams
import com.nutricionbn.insulator.ui.components.calculation.PatientSettingsCard
import com.nutricionbn.insulator.ui.viewModels.calculation.CalculationViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CalculationScreen(
    viewModel: CalculationViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val foodList by viewModel.filteredList.collectAsStateWithLifecycle()

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
            foodList = foodList,
            searchQuery = searchQuery,
            onQueryChange = { value -> viewModel.onSearchQueryChange(value) },
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
                DoseCalculation(
                    onCalculation = { viewModel.onCalculateClicked() },
                    onConfirmation = { viewModel.onConfirmationClicked() },
                    onConfirmationConfirmed = { observations ->
                        viewModel.onConfirmationConfirmed(
                            observations
                        )
                    },
                    onConfirmationDismissed = { viewModel.onConfirmationDismissed() },
                    onDismissConfirmationSuccessMessage = {
                        viewModel.dismissSuccessMessage {
                            it.copy(
                                loadingConfirmationSuccess = false
                            )
                        }
                    },
                    showConfirmationButton = uiState.showConfirmationButton,
                    loadingDoseConfirmation = uiState.loadingConfirmation,
                    loadingConfirmationError = uiState.loadingConfirmationError,
                    confirmationLoadingSuccess = uiState.loadingConfirmationSuccess,
                    showConfirmationDialog = uiState.showDoseCalculationConfirmationDialog,
                    isPatient = uiState.isPatient
                )
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

    if (uiState.showPendingSnapshotConfirmationDialog) {
        val formattedTime = formatSnapshotTime(uiState.lastDoseCalculationSnapshot!!.calculatedAt)

        DoseConfirmationDialog(
            title = "Dosis pendiente de confirmación",
            message = "Tenés una dosis pendiente de confirmación calculada $formattedTime.",
            confirmText = "Confirmar",
            dismissText = "Descartar",
            isLoading = uiState.loadingPendingSnapshot,
            onConfirm = { observations ->
                viewModel.onPendingSnapshotConfirmation(observations)
            },
            onDismiss = {
                viewModel.onDiscardPendingSnapshot()
            }
        )
    }

    if (uiState.showUnsavedWarningDialog) {
        DoseConfirmationDialog(
            title = "Atención",
            message = "Primero debes confirmar la dosis que calculaste, o descartarla.",
            confirmText = "Confirmar",
            dismissText = "Descartar",
            isLoading = uiState.loadingConfirmation,
            onConfirm = { observations ->
                viewModel.onConfirmationConfirmed(observations)
            },
            onDismiss = {
                viewModel.onWarningDialogDismissed()
            }
        )
    }

    if (uiState.error.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onErrorDismissed()
            },
            title = {
                Text(text = uiState.error)
            },
            text = {
                Text(text = "Algo salió mal al momento de procesar la solicitud")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onErrorDismissed()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

fun formatSnapshotTime(calculatedAt: String): String {
    try {
        if (calculatedAt.isEmpty()) return ""

        val instant = Instant.parse(calculatedAt)
        val argentinaZone = ZoneId.of("America/Argentina/Buenos_Aires")
        val argentinaDateTime = instant.atZone(argentinaZone)

        val targetDate = argentinaDateTime.toLocalDate()
        val today = LocalDate.now(argentinaZone)

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val timeString = argentinaDateTime.format(timeFormatter)

        val dateString = if (targetDate == today) {
            "hoy"
        } else {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
            targetDate.format(dateFormatter)
        }

        return "a las $timeString" + if (targetDate == today) " de $dateString" else " del $dateString"
    } catch (e: Exception) {
        return ""
    }
}