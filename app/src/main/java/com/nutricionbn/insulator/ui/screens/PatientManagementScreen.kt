package com.nutricionbn.insulator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PatientManagementScreen(
    onNavigateToPatientAssignmentScreen: () -> Unit,
    onNavigateToMyPatientsScreen: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row() {
            Button(modifier = Modifier.width(200.dp), onClick = onNavigateToPatientAssignmentScreen) {
                Text("Asignar paciente")
            }
        }
        Row() {
            Button(modifier = Modifier.width(200.dp), onClick = onNavigateToMyPatientsScreen) {
                Text("Mis pacientes")
            }
        }
    }
}