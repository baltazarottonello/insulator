package com.nutricionbn.insulator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import com.nutricionbn.insulator.ui.components.ErrorBox
import com.nutricionbn.insulator.ui.components.SuccessMessage
import com.nutricionbn.insulator.ui.components.management.patients.EmailSearchBar
import com.nutricionbn.insulator.ui.components.management.patients.PotentialPatientCard
import com.nutricionbn.insulator.ui.viewModels.management.patients.SearchUserUiState
import com.nutricionbn.insulator.ui.viewModels.management.patients.SearchUserViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PatientAssignmentScreen(viewModel: SearchUserViewModel = koinViewModel()) {
    val uiState: SearchUserUiState by viewModel.searchUserUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var showEmailError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .verticalScroll(scrollState)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = if (uiState.isLoading) Alignment.CenterHorizontally else
            Alignment.Start
    ) {
        EmailSearchBar(
            showEmailError = showEmailError,
            onValidationError = { showEmailError = true },
            handler = { email ->
                showEmailError = false
                viewModel.onSearchUserByEmail(email)
            })
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.userLoaded) {
            PotentialPatientCard(
                user = uiState.user,
                loading = uiState.assigningPatient,
                onAssignClick = { viewModel.onAssignAsPatient(uiState.user) }
            )
        } else if (uiState.error != "") {
            ErrorBox(uiState.error!!)
        } else if (uiState.patientAssignmentSuccess) {
            SuccessMessage(
                text = "Paciente asignado con éxito",
                onDismiss = {
                    viewModel.dismissSuccessMessage { it.copy(patientAssignmentSuccess = false) }
                }
            )
        }
    }
}
