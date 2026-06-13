package com.example.insulator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulator.ui.components.management.ManagementDashboard
import com.example.insulator.ui.viewModels.management.ManagementViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManagementScreen(
    viewModel: ManagementViewModel = koinViewModel(),
    onNavigateToPatientsManagement: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

//    LaunchedEffect(uiState.isSuccess) {
//        if (uiState.isSuccess) {
//            onNavigateToCalculation()
//        }
//    }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(scrollState)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        ManagementDashboard(isMedic = uiState.isMedic, onNavigateToPatientsManagement)
    }
}