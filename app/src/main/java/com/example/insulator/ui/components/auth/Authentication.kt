package com.example.insulator.ui.components.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.insulator.R
import com.example.insulator.ui.components.ErrorBox

@Composable
fun Authentication(
    isSuccess: Boolean,
    error: String?,
    onGoogleSignInClick: () -> Unit,
    interactionDisabled: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!isSuccess) {
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                Button(
                    onClick = onGoogleSignInClick,
                    border = BorderStroke(1.dp, Color(0xFF747775)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1F1F1F),
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.Gray,
                    ),
                    enabled = !interactionDisabled
                ) {
                    Image(
                        painter = painterResource(R.drawable.google_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Iniciar sesión con Google")
                }
            }
        } else null

        error?.let { errorText ->
            if (error != "") {
                ErrorBox(text = errorText)
            } else null
        }
    }
}
