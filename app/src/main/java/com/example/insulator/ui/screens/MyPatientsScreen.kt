package com.example.insulator.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulator.ui.components.management.patients.PatientCard
import com.example.insulator.ui.viewModels.management.patients.MyPatientsViewModel
import com.example.insulator.ui.viewModels.management.patients.PatientParams
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyPatientsScreen(
    viewModel: MyPatientsViewModel = koinViewModel(),
) {
    val uiState by viewModel.myPatientsUiState.collectAsStateWithLifecycle()
    val paramsHistoryUiState by viewModel.patientParamsHistoryUiState.collectAsStateWithLifecycle()

    Scaffold { padding ->
        if (uiState.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.patients.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No tienes pacientes asignados",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(uiState.patients) { patient ->
                    PatientCard(
                        updating = uiState.updating,
                        loadingParamsHistory = paramsHistoryUiState.isLoading,
                        error = !uiState.error.isEmpty(),
                        errorMessage = uiState.error,
                        patient = patient,
                        updateSuccessful = uiState.updateSuccessful,
                        onSave = { updatedPatient ->
                            viewModel.updatePatientParams(
                                PatientParams(
                                    updatedPatient.id,
                                    updatedPatient.ratio.toString(),
                                    updatedPatient.sensitivity.toString(),
                                    updatedPatient.targetGlucose.toString()
                                )
                            )
                        },
                        onSeePatientParamsHistory = { patient -> viewModel.loadHistory(patient.id) },
                        historyData = paramsHistoryUiState.data
                    )
                }
            }
        }
    }
}