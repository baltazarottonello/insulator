package com.example.insulator.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.insulator.BuildConfig
import com.example.insulator.core.viewModels.AuthenticationViewModel
import com.example.insulator.ui.components.auth.Authentication
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = koinViewModel(),
    onNavigateToCalculation: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToCalculation()
        }
    }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(scrollState)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Authentication(
            isLoading = uiState.isLoading,
            isSuccess = uiState.isSuccess,
            error = uiState.error,
            onGoogleSignInClick = {
                coroutineScope.launch {
                    try {
                        val credentialManager = CredentialManager.create(context)

                        val googleIdOption =
                            GetSignInWithGoogleOption.Builder(serverClientId = BuildConfig.GOOGLE_CLIENT_ID).build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                        val result =
                            credentialManager.getCredential(context = context, request = request)

                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(result.credential.data)
                        val idToken = googleIdTokenCredential.idToken

                        viewModel.onGoogleSignInComplete(idToken)

                    } catch (e: GetCredentialException) {
                        viewModel.onSignInError("Cancelado o error: ${e.message}")
                    } catch (e: GoogleIdTokenParsingException) {
                        viewModel.onSignInError("Error al procesar el token de Google")
                    }
                }
            })
    }
}