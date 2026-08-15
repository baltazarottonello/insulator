package com.nutricionbn.insulator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun DoseConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "Aceptar",
    dismissText: String = "Cancelar",
    isLoading: Boolean,
    onConfirm: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var textValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        title = {
            Text(text = title)
        },
        text = {
            Column {
                Text(text = message, modifier = Modifier.padding(bottom = 8.dp))

                OutlinedTextField(
                    value = textValue,
                    onValueChange = { newValue ->
                        if (newValue.length <= 140) {
                            textValue = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("(Opcional) Aclara algo sobre esta dosis...") },
                    maxLines = 3,
                    supportingText = {
                        Text(
                            text = "${textValue.length}/140",
                            modifier = Modifier.fillMaxWidth(),
                            color = if (textValue.length >= 140) Color.Red else Color.Gray
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalResult = textValue.ifBlank { null }
                    onConfirm(finalResult)
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(25.dp), color = Color.White)
                } else {
                    Text(text = confirmText)
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text(text = dismissText)
            }
        }
    )
}