package com.nutricionbn.insulator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutricionbn.insulator.BuildConfig
import com.nutricionbn.insulator.ui.components.Footer
import com.nutricionbn.insulator.ui.components.auth.Authentication
import com.nutricionbn.insulator.ui.viewModels.auth.AuthenticationViewModel
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A8FA3),
                        Color(0xFF0F6B5C)
                    ),
                    radius = 800f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = if (uiState.isLoading) Arrangement.Center else Arrangement.SpaceBetween
        ) {
            if (uiState.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                Column(
                    modifier = Modifier
                        .height(250.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Authentication(
                        isSuccess = uiState.isSuccess,
                        interactionDisabled = uiState.interactionDisabled,
                        error = uiState.error,
                        onGoogleSignInClick = {
                            coroutineScope.launch {
                                try {
                                    viewModel.onGoogleSignInStarting()
                                    val credentialManager = CredentialManager.create(context)

                                    val googleIdOption =
                                        GetSignInWithGoogleOption.Builder(serverClientId = BuildConfig.GOOGLE_CLIENT_ID)
                                            .build()

                                    val request = GetCredentialRequest.Builder()
                                        .addCredentialOption(googleIdOption)
                                        .build()

                                    val result =
                                        credentialManager.getCredential(
                                            context = context,
                                            request = request
                                        )

                                    val googleIdTokenCredential =
                                        GoogleIdTokenCredential.createFrom(result.credential.data)
                                    val idToken = googleIdTokenCredential.idToken

                                    viewModel.onGoogleSignInComplete(idToken)

                                } catch (e: GetCredentialException) {
                                    viewModel.onSignInError("Cancelado, algo salió mal")
                                } catch (e: GoogleIdTokenParsingException) {
                                    viewModel.onSignInError("Error al procesar el token de Google")
                                }
                            }
                        })
                }
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Footer(isSuccess = uiState.isSuccess)
                }
            }
        }
    }
}
