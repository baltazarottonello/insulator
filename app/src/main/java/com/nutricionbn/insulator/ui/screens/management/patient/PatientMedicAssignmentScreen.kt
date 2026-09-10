package com.nutricionbn.insulator.ui.screens.management.patient

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutricionbn.insulator.ui.components.ErrorBox
import com.nutricionbn.insulator.ui.components.management.medics.MedicCard
import com.nutricionbn.insulator.ui.viewModels.management.patients.PatientMedicAssignmentViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PatientMedicAssignmentScreen(
    viewModel: PatientMedicAssignmentViewModel = koinViewModel(), onNavigateAway: () -> Unit
) {

    val uiState by viewModel.patientMedicAssignmentUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.shouldNavigateAway) {
        if (uiState.shouldNavigateAway) {
            onNavigateAway()
            viewModel.onNavigationHandled()
        }
    }

    Scaffold { padding ->
        if (uiState.error.isNotBlank()) {
            ErrorBox(uiState.error, onDismiss = {})
        }
        if (uiState.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.medics.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No tienes médicos asignados",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
            ) {
                items(uiState.medics) { medic ->
                    MedicCard(
                        modalLoading = uiState.modalLoading,
                        showUnlinkMedicConfirmationModal = uiState.showUnlinkMedicConfirmationModal,
                        showUnlinkMedicSuccessModal = uiState.showUnlinkMedicSuccessModal,
                        errorMessage = uiState.error,
                        medic = medic,
                        onUnlinkMedicClick = { viewModel.onUnlinkMedicClick() },
                        onUnlinkMedicConfirmation = { medicId ->
                            viewModel.onUnlinkMedicConfirmation(
                                medicId
                            )
                        },
                        onUnlinkMedicDismiss = { viewModel.onUnlinkMedicDismiss() },
                        onUnlinkMedicSuccessDismiss = { viewModel.onUnlinkMedicSuccessDismiss() }
                    )
                }
            }
        }
    }
}
