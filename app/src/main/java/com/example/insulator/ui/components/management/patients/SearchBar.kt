package com.example.insulator.ui.components.management.patients

import android.util.Patterns
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.insulator.R

@Composable
fun EmailSearchBar(
    showEmailError: Boolean,
    handler: (String) -> Unit,
    onValidationError: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    fun trySearch() {
        if (Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            handler(emailText)
            keyboardController?.hide()
        } else {
            onValidationError()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        OutlinedTextField(
            value = emailText,
            onValueChange = { emailText = it },
            label = { Text("Introducí el mail de un usuario") },
            placeholder = { Text("ejemplo@correo.com") },
            isError = showEmailError,
            singleLine = true,
            supportingText = {
                if (showEmailError) {
                    Text(
                        text = "Ingresá un email válido",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (emailText.isNotBlank()) {
                        trySearch()
                    }
                }
            ),
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.magnifying_glass_solid_full),
                    contentDescription = "Icono de búsqueda"
                )
            },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier
            .width(8.dp)
            .height(1.dp))
    }
}