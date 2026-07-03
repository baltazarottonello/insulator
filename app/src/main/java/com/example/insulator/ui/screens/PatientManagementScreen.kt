package com.example.insulator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
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
import com.example.insulator.ui.viewModels.management.patients.PatientManagementViewModel
import com.example.insulator.ui.viewModels.management.patients.PatientState
import org.koin.androidx.compose.koinViewModel

@Composable
fun PatientManagementScreen(
    viewModel: PatientManagementViewModel = koinViewModel(),
    onNavigateToSearchPatientScreen: () -> Unit,
    onNavigateToMyPatientsScreen: () -> Unit
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

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Row() {
            Button(modifier = Modifier.width(200.dp), onClick = onNavigateToSearchPatientScreen) {
                Text("Buscar paciente")
            }
        }
        Row() {
            Button(modifier = Modifier.width(200.dp), onClick = onNavigateToMyPatientsScreen) {
                Text("Mis pacientes")
            }
        }
    }
}