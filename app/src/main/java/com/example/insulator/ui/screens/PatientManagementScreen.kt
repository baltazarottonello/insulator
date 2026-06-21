package com.example.insulator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulator.core.data.dto.patient.PatientParameters
import com.example.insulator.ui.components.ErrorBox
import com.example.insulator.ui.components.SuccessMessage
import com.example.insulator.ui.components.calculation.PatientSettingsCard
import com.example.insulator.ui.components.management.patients.EmailSearchBar
import com.example.insulator.ui.viewModels.management.patients.PatientManagementViewModel
import com.example.insulator.ui.viewModels.management.patients.PatientState
import org.koin.androidx.compose.koinViewModel

@Composable
fun PatientManagementScreen(
    viewModel: PatientManagementViewModel = koinViewModel(),
) {
    val uiState by viewModel.patientManagementUiState.collectAsStateWithLifecycle()
    val patientState: PatientState by viewModel.patientState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var ratio by remember { mutableStateOf("") }
    var sens by remember { mutableStateOf("") }
    var targetGlucose by remember { mutableStateOf("") }

    var showRatioError by remember { mutableStateOf(false) }
    var showSensError by remember { mutableStateOf(false) }
    var showTargetGlucoseError by remember { mutableStateOf(false) }
    var showEmailError by remember { mutableStateOf(false) }

//    LaunchedEffect(uiState.isSuccess) {
//        if (uiState.isSuccess) {
//            onNavigateToCalculation()
//        }
//    }

    fun validateAndSubmit() {
        showRatioError = ratio.toFloatOrNull() == null
        showSensError = sens.toFloatOrNull() == null
        showTargetGlucoseError = targetGlucose.toFloatOrNull() == null

        if (!showRatioError && !showSensError && !showTargetGlucoseError) {
            viewModel.onSetPatientParams(
                id = patientState.id,
                params = PatientParameters(
                    ratio = ratio.toFloat(),
                    sens = sens.toFloat(),
                    targetGlucose = targetGlucose.toFloat()
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(scrollState)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = if (uiState.isLoading) Alignment.CenterHorizontally else
            Alignment.Start
    ) {
        EmailSearchBar(
            showEmailError = showEmailError,
            onValidationError = { showEmailError = true },
            handler = { email ->
                showEmailError = false
                viewModel.onSearchPatientByEmail(email)
            })
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.patientLoaded) {
            PatientSettingsCard(
                ratio = ratio,
                showRatioError = showRatioError,
                onRatioChange = { newValue -> ratio = newValue; showRatioError = false },
                sensitivity = sens,
                showSensError = showSensError,
                onSensitivityChange = { newValue -> sens = newValue; showSensError = false },
                targetGlucose = targetGlucose,
                showTargetGlucoseError = showTargetGlucoseError,
                onTargetGlucoseChange = { newValue ->
                    targetGlucose = newValue; showTargetGlucoseError = false
                },
                enabled = true
            )
            ElevatedButton(
                enabled = !uiState.isLoading,
                modifier = Modifier,
                onClick = {
                    validateAndSubmit()
                },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Cargar")
                }
            }
        } else if (uiState.error != "") {
            ErrorBox(uiState.error!!)
        } else if (uiState.isSuccess) {
            SuccessMessage(
                text = "Parámetros cargados con éxito",
                subtitle = "Para modificar volve a darle a \"Buscar\""
            )
        }
    }
}