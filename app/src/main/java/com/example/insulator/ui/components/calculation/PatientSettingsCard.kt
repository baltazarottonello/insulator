package com.example.insulator.ui.components.calculation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.insulator.R

@Composable
fun PatientSettingsCard(
    modifier: Modifier = Modifier,
    ratio: String,
    showRatioError: Boolean = false,
    onRatioChange: (String) -> Unit = {},
    sensitivity: String,
    showSensError: Boolean = false,
    onSensitivityChange: (String) -> Unit = {},
    targetGlucose: String,
    showTargetGlucoseError: Boolean = false,
    onTargetGlucoseChange: (String) -> Unit = {},
    enabled: Boolean,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9ECEF)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.sliders_solid_full),
                    contentDescription = "Ajustes",
                    tint = Color(0xFF6C757D),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AJUSTES DEL PACIENTE",
                    color = Color(0xFF6C757D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = Color(0xFFCED4DA), thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LabeledTextField(
                    label = "RATIO (G/U)",
                    value = ratio,
                    onValueChange = onRatioChange,
                    modifier = Modifier.weight(1f),
                    isError = showRatioError,
                    supportingText = if (showRatioError) {
                        {
                            Text(
                                text = "Este campo es obligatorio",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ), enabled = true //TODO true por default hasta que se implemente de otra manera
                )
                LabeledTextField(
                    label = "SENSIBILIDAD",
                    value = sensitivity,
                    onValueChange = onSensitivityChange,
                    modifier = Modifier.weight(1f),
                    isError = showSensError,
                    supportingText = if (showSensError) {
                        {
                            Text(
                                text = "Este campo es obligatorio",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ), enabled = enabled
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LabeledTextField(
                label = "GLUCOSA OBJETIVO (MG/DL)",
                value = targetGlucose,
                onValueChange = onTargetGlucoseChange,
                modifier = Modifier.fillMaxWidth(),
                isError = showTargetGlucoseError,
                supportingText = if (showTargetGlucoseError) {
                    {
                        Text(
                            text = "Este campo es obligatorio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ), enabled = enabled
            )
        }
    }
}

@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    label: String,
    value: String, onValueChange: (String) -> Unit = {},
    isError: Boolean,
    supportingText: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6C757D)
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            enabled = enabled,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color(0xFFCED4DA),
                focusedBorderColor = Color.Black
            ),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            isError = isError,
            supportingText = supportingText
        )
    }
}