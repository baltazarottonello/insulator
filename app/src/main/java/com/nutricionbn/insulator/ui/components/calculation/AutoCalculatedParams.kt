package com.nutricionbn.insulator.ui.components.calculation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nutricionbn.insulator.R

@Composable
fun AutoCalculatedParams(
    glucose: String,
    showGlucoseError: Boolean,
    glucoseOutOfRange: Boolean,
    carbs: String,
    showCarbsError: Boolean,
    carbsOutOfRange: Boolean,
    onGlucoseChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Box() {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(R.drawable.droplet_solid_full),
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                },
                onValueChange = { onGlucoseChange(it) },
                value = glucose,
                label = {
                    Text(
                        "Glucosa en sangre (mg/dL)".uppercase(),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                placeholder = { Text("Ej: 140") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                isError = showGlucoseError || glucoseOutOfRange,
                supportingText = when {
                    showGlucoseError -> {
                        {
                            Text(
                                text = "Este campo es obligatorio",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    glucoseOutOfRange -> {
                        {
                            Text(
                                text = "El valor está fuera del rango",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    else -> null
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .background(Color(0xFFFEFCE8))
                    )
                },
                onValueChange = { onCarbsChange(it) },
                value = carbs,
                label = {
                    Text(
                        "Carbohidratos totales (g)".uppercase(),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                placeholder = { Text("Ej: 0") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                isError = showCarbsError || carbsOutOfRange,
                supportingText = when {
                    showCarbsError -> {
                        {
                            Text(
                                text = "Este campo es obligatorio",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    carbsOutOfRange -> {
                        {
                            Text(
                                text = "El valor está fuera del rango",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    else -> null
                }
            )
        }
    }
}