package com.nutricionbn.insulator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutricionbn.insulator.ui.components.profile.UserProfileSettings
import com.nutricionbn.insulator.ui.viewModels.profile.UserProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        UserProfileSettings(
            isDataChanged = uiState.isDataChanged,
            firstName = uiState.name,
            lastName = uiState.lastName,
            modalLoading = uiState.modalLoading,
            onFirstNameChanged = { newName -> viewModel.onFirstNameChanged(newName) },
            onLastNameChanged = { newLastName -> viewModel.onLastNameChanged(newLastName) },
            email = uiState.email,
            onDeleteAccountClick = { viewModel.onDeleteAccountClick() },
            showDeleteAccountConfirmationModal = uiState.showDeleteAccountConfirmationModal,
            onDeleteAccountConfirmed = { viewModel.onDeleteAccountConfirmed() },
            onDeleteAccountDismiss = { viewModel.onDeleteAccountDismiss() },
            accountDeletionSuccess = uiState.accountDeletionSuccess,
            onSaveClick = { viewModel.onSaveClick() },
            showSaveChangesConfirmationModal = uiState.showSaveChangesConfirmationModal,
            onSaveConfirmed = { viewModel.onSaveConfirmed() },
            onSaveDismiss = { viewModel.onSaveDismiss() },
            onSignOutClick = { viewModel.onSignOutClick() },
            showSaveChangesSuccessModal = uiState.showSaveChangesSuccessModal,
            onDismissSaveChangesSuccessModal = { viewModel.onDismissSaveChangesSuccessModal() },
            showSignOutConfirmationModal = uiState.showSignOutConfirmationModal,
            onSignOutConfirmed = {
                coroutineScope.launch {
                    viewModel.onSignOutConfirmed()
                }
            },
            onSignOutDismiss = { viewModel.onSignOutDismiss() }
        )
    }

    if (uiState.error.isNotBlank()) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onErrorDialogDismissed()
            },
            title = {
                Text(text = uiState.error)
            },
            text = {
                Text(text = "Algo salió mal al momento de procesar la solicitud")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onErrorDialogDismissed()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}