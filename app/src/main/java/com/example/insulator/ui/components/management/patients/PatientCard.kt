package com.example.insulator.ui.components.management.patients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.insulator.R
import com.example.insulator.core.data.dto.patient.PatientCard
import com.example.insulator.core.data.dto.patient.PatientParamsHistoryRow
import com.example.insulator.ui.components.ErrorBox
import com.example.insulator.ui.components.SuccessMessage

@Composable
fun PatientCard(
    updateSuccessful: Boolean,
    error: Boolean,
    errorMessage: String,
    updating: Boolean,
    loadingParamsHistory: Boolean,
    historyData: List<PatientParamsHistoryRow>,
    patient: PatientCard,
    onSave: (PatientCard) -> Unit,
    onSeePatientParamsHistory: (PatientCard) -> Unit
) {
    var patientCardExpanded by remember { mutableStateOf(false) }
    var patientParamsHistoryExpanded by remember { mutableStateOf(false) }

    var sensitivity by remember { mutableStateOf(patient.sensitivity.toString()) }
    var ratio by remember { mutableStateOf(patient.ratio.toString()) }
    var targetGlucose by remember { mutableStateOf(patient.targetGlucose.toString()) }

    val isSensitivityValid = sensitivity.isNotBlank() && sensitivity.toFloatOrNull() != null
    val isRatioValid = ratio.isNotBlank() && ratio.toFloatOrNull() != null
    val isTargetGlucoseValid = targetGlucose.isNotBlank() && targetGlucose.toFloatOrNull() != null

    val isChanged = (sensitivity.toFloatOrNull() != patient.sensitivity) ||
            (ratio.toFloatOrNull() != patient.ratio) ||
            (targetGlucose.toFloatOrNull() != patient.targetGlucose)

    val isFormValid = isSensitivityValid && isRatioValid && isTargetGlucoseValid

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { patientCardExpanded = !patientCardExpanded },
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
                        text = "${patient.lastName}, ${patient.name}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = patient.email, style = MaterialTheme.typography.bodySmall)
                }
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = if (patientCardExpanded) painterResource(R.drawable.angle_up_solid_full) else painterResource(
                        R.drawable.angle_down_solid_full
                    ),
                    contentDescription = null
                )
            }

            AnimatedVisibility(visible = patientCardExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    OutlinedTextField(
                        value = ratio,
                        onValueChange = { ratio = it },
                        label = { Text("Ratio") },
                        isError = !isRatioValid,
                        supportingText = {
                            if (!isRatioValid) Text("Ingresa un número válido")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = sensitivity,
                        onValueChange = { sensitivity = it },
                        label = { Text("Sensibilidad") },
                        isError = !isSensitivityValid,
                        supportingText = {
                            if (!isSensitivityValid) Text("Ingresa un número válido")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = targetGlucose,
                        onValueChange = { targetGlucose = it },
                        label = { Text("Glucosa objetivo") },
                        isError = !isTargetGlucoseValid,
                        supportingText = {
                            if (!isTargetGlucoseValid) Text("Ingresa un número válido")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (updateSuccessful) {
                        SuccessMessage("Parámetros cargados con éxito!")
                    } else if (error) {
                        ErrorBox(errorMessage)
                    } else {
                        if (!isChanged && isFormValid) {
                            Text(
                                text = "Debes modificar al menos un valor para guardar",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        Button(
                            onClick = {
                                onSave(
                                    patient.copy(
                                        sensitivity = sensitivity.toFloat(),
                                        ratio = ratio.toFloat(),
                                        targetGlucose = targetGlucose.toFloat()
                                    )
                                )
                            },
                            enabled = isChanged && isFormValid,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                        ) {
                            if (updating) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text("Guardar cambios")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                patientParamsHistoryExpanded = !patientParamsHistoryExpanded
                                if (patientParamsHistoryExpanded && historyData.isEmpty() && !loadingParamsHistory) {
                                    onSeePatientParamsHistory(patient)
                                }
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ver historial de parámetros cargados",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = if (patientParamsHistoryExpanded) painterResource(R.drawable.angle_up_solid_full) else painterResource(
                                R.drawable.angle_down_solid_full
                            ),
                            contentDescription = null
                        )
                    }

                    AnimatedVisibility(visible = patientParamsHistoryExpanded) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            if (loadingParamsHistory) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                            } else {
                                historyData.forEach { row ->
                                    HistoryRowItem(row)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}