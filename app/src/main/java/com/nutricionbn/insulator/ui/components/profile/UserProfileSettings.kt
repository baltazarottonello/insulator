package com.nutricionbn.insulator.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutricionbn.insulator.ui.components.CustomDialog
import com.nutricionbn.insulator.ui.components.ErrorBox
import com.nutricionbn.insulator.ui.components.GenericConfirmationDialog
import com.nutricionbn.insulator.ui.components.SuccessMessage

@Composable
fun UserProfileSettings(
    isDataChanged: Boolean,
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    email: String,
    onDeleteAccountClick: () -> Unit,
    showDeleteAccountConfirmationModal: Boolean,
    onDeleteAccountConfirmed: () -> Unit,
    onDeleteAccountDismiss: () -> Unit,
    accountDeletionSuccess: Boolean,
    onSaveClick: () -> Unit,
    showSaveChangesConfirmationModal: Boolean,
    onSaveConfirmed: () -> Unit,
    onSaveDismiss: () -> Unit,
    onSignOutClick: () -> Unit,
    showSignOutConfirmationModal: Boolean,
    onSignOutConfirmed: () -> Unit,
    onSignOutDismiss: () -> Unit,
    showSaveChangesSuccessModal: Boolean,
    onDismissSaveChangesSuccessModal: () -> Unit,
    modalLoading: Boolean,
) {
    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = onFirstNameChanged,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !modalLoading
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChanged,
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !modalLoading
            )

            OutlinedTextField(
                value = email,
                onValueChange = {},
                enabled = false,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = onSaveClick,
                enabled = isDataChanged && isFormValid && !modalLoading,
            ) {
                if (modalLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar")
                }
            }
        }


        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Desactivar cuenta de Insulator", fontSize = 20.sp)
            Button(
                onClick = onDeleteAccountClick,
                enabled = !modalLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Eliminar mi cuenta y todos mis datos")
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = onSignOutClick) {
                Text("Cerrar sesión")
            }
        }
    }


    if (showSaveChangesConfirmationModal) {
        GenericConfirmationDialog(
            title = "Guardar cambios",
            message = "¿Estás seguro de que deseas actualizar tu información personal?",
            confirmText = "Guardar",
            dismissText = "Cancelar",
            isLoading = modalLoading,
            onConfirm = { onSaveConfirmed() },
            onDismiss = onSaveDismiss
        )
    }

    if (showDeleteAccountConfirmationModal) {
        GenericConfirmationDialog(
            title = "Eliminar cuenta",
            message = "Esta acción es irreversible. Se eliminarán tu cuenta y todos tus datos asociados.",
            confirmText = "Eliminar",
            dismissText = "Cancelar",
            isLoading = modalLoading,
            onConfirm = { onDeleteAccountConfirmed() },
            onDismiss = onDeleteAccountDismiss
        )
    }

    if (showSignOutConfirmationModal) {
        GenericConfirmationDialog(
            title = "Cerrar sesión",
            message = "¿Estás seguro de que quieres cerrar sesión?",
            confirmText = "Si",
            dismissText = "No",
            isLoading = modalLoading,
            onConfirm = { onSignOutConfirmed() },
            onDismiss = onSignOutDismiss,
        )
    }

    if (showSaveChangesSuccessModal) {
        CustomDialog(onDismissRequest = onDismissSaveChangesSuccessModal) {
            SuccessMessage(
                "Datos guardados con éxito",
                subtitle = "Tus datos han sido actualizados",
                onDismiss = onDismissSaveChangesSuccessModal
            )
        }
    }

    if (accountDeletionSuccess) {
        CustomDialog(onDismissRequest = onSignOutConfirmed) {
            SuccessMessage(
                "Tu cuenta ha sido eliminada",
                subtitle = "Se cerrará la sesión",
                onDismiss = onSignOutConfirmed
            )
        }
    }
}
