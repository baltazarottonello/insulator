package com.nutricionbn.insulator.ui.components.management.medics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nutricionbn.insulator.core.data.dto.medic.PatientMedicCard
import com.nutricionbn.insulator.ui.components.CustomDialog
import com.nutricionbn.insulator.ui.components.ErrorBox
import com.nutricionbn.insulator.ui.components.GenericConfirmationDialog
import com.nutricionbn.insulator.ui.components.SuccessMessage
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MedicCard(
    modalLoading: Boolean,
    showUnlinkMedicConfirmationModal: Boolean,
    showUnlinkMedicSuccessModal: Boolean,
    errorMessage: String?,
    medic: PatientMedicCard,
    onUnlinkMedicClick: () -> Unit,
    onUnlinkMedicConfirmation: (medicId: String) -> Unit,
    onUnlinkMedicDismiss: () -> Unit,
    onUnlinkMedicSuccessDismiss: () -> Unit
) {

    val instant = Instant.parse(medic.medicSince)
    val argentinaZone = ZoneId.of("America/Argentina/Buenos_Aires")
    val argentinaDateTime = instant.atZone(argentinaZone)
    val targetDate = argentinaDateTime.toLocalDate()

    val formattedDate = targetDate.format(DateTimeFormatter.ofPattern("dd/MM/yy"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${medic.lastName}, ${medic.name}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = medic.email, style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "Es tu médico desde: $formattedDate",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (errorMessage!!.isNotBlank()) {
                        ErrorBox(errorMessage, onDismiss = {})
                    }
                    Button(
                        onClick = onUnlinkMedicClick,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    ) {
                        if (modalLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text("Desvincular")
                        }
                    }
                }
            }
        }

        if (showUnlinkMedicConfirmationModal) {
            GenericConfirmationDialog(
                "¿Desvincular médico?",
                "Vas a dejar de ser paciente de este médico",
                "Confirmar",
                "Cancelar",
                modalLoading,
                { onUnlinkMedicConfirmation(medic.id) },
                onUnlinkMedicDismiss
            )
        }

        if (showUnlinkMedicSuccessModal) {
            CustomDialog(onUnlinkMedicSuccessDismiss) {
                SuccessMessage(
                    "Médico desvinculado",
                    "Ya no eres paciente de este médico",
                    onDismiss = onUnlinkMedicSuccessDismiss
                )
            }
        }
    }
}