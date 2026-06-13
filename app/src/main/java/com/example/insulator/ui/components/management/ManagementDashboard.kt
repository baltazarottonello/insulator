package com.example.insulator.ui.components.management

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.insulator.ui.theme.InsulatorTheme

@Composable
fun ManagementDashboard(isMedic: Boolean, toPatientsManagement: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row() {
            Button(modifier = Modifier.fillMaxWidth(), onClick = toPatientsManagement) {
                Text("Pacientes")
            }
        }

    }
}

@Preview
@Composable
fun Preview() {
    InsulatorTheme() {
        ManagementDashboard(isMedic = true, toPatientsManagement = {})
    }
}