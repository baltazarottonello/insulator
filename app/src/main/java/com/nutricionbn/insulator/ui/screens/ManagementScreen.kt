package com.nutricionbn.insulator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nutricionbn.insulator.ui.components.management.ManagementDashboard
import com.nutricionbn.insulator.ui.viewModels.management.ManagementViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManagementScreen(
    viewModel: ManagementViewModel = koinViewModel(),
    onNavigateToPatientsManagement: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(scrollState)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        ManagementDashboard(onNavigateToPatientsManagement)
    }
}