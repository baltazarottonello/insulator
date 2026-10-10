package com.nutricionbn.insulator.ui.screens.management.medic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutricionbn.insulator.ui.components.management.patients.PatientCard
import com.nutricionbn.insulator.ui.viewModels.management.medics.MyPatientsViewModel
import com.nutricionbn.insulator.ui.viewModels.management.medics.PatientDosesHistoryState
import com.nutricionbn.insulator.ui.viewModels.management.medics.PatientParams
import com.nutricionbn.insulator.ui.viewModels.management.medics.PatientParamsHistoryState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MedicPatientsScreen(
    viewModel: MyPatientsViewModel = koinViewModel(),
) {
    val uiState by viewModel.myPatientsUiState.collectAsStateWithLifecycle()

    Scaffold { padding ->

        if (uiState.error.isNotBlank()) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.onErrorDialogDismissed()
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
                            viewModel.onErrorDialogDismissed()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        if (uiState.loadingPatients) {
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
                    .padding(horizontal = 16.dp),
            ) {
                items(uiState.patients) { patient ->
                    PatientCard(
                        updatingParams = uiState.updatingParams,
                        loadingParamsHistory = uiState.paramsHistoryStates.find { it.patientId == patient.id }?.isLoading
                            ?: false,
                        loadingDosesHistory = uiState.dosesHistoryStates.find { it.patientId == patient.id }?.isLoading
                            ?: false,
                        patient = patient,
                        paramsUpdateSuccessful = uiState.paramsUpdateSuccessful,
                        onDismissParamsUpdateSuccessful = {
                            viewModel.dismissSuccessMessage {
                                it.copy(
                                    paramsUpdateSuccessful = false
                                )
                            }
                        },
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
                        onSeePatientParamsHistory = { patient ->
                            viewModel.loadParamsHistory(
                                patient.id
                            )
                        },
                        onSeePatientDosesHistory = { patient ->
                            viewModel.loadDosesHistory(
                                patient.id
                            )
                        },
                        paramsHistoryState = uiState.paramsHistoryStates.find { it.patientId == patient.id }
                            ?: PatientParamsHistoryState(patientId = patient.id),
                        dosesHistoryState = uiState.dosesHistoryStates.find { it.patientId == patient.id }
                            ?: PatientDosesHistoryState(patientId = patient.id),
                        onPatientParamsDataErrorDialogDismissed = { patientId ->
                            viewModel.onPatientParamsDataErrorDialogDismissed(
                                patientId
                            )
                        },
                        onPatientDosesDataErrorDialogDismissed = { patientId ->
                            viewModel.onPatientDosesDataErrorDialogDismissed(patientId)
                        }
                    )
                }
            }
        }
    }
}