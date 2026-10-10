package com.nutricionbn.insulator.ui.components.calculation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nutricionbn.insulator.R
import com.nutricionbn.insulator.ui.components.DoseConfirmationDialog
import com.nutricionbn.insulator.ui.components.SuccessMessage

@Composable
fun DoseCalculation(
    showConfirmationButton: Boolean,
    loadingDoseConfirmation: Boolean,
    confirmationLoadingSuccess: Boolean,
    isPatient: Boolean,
    showConfirmationDialog: Boolean,
    onCalculation: () -> Unit,
    onConfirmation: () -> Unit,
    onConfirmationConfirmed: (String?) -> Unit,
    onConfirmationDismissed: () -> Unit,
    onDismissConfirmationSuccessMessage: () -> Unit,
) {

    val softGreen = Color(0xFF2E7D32)

    val animatedContainerColor by animateColorAsState(
        targetValue = if (showConfirmationButton) softGreen else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 500),
        label = "ButtonColorAnimation"
    )

    if (!showConfirmationButton) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onCalculation,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = animatedContainerColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(R.drawable.syringe_solid_full),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Calcular")
            }
            if (confirmationLoadingSuccess) {
                SuccessMessage(
                    "Dosis confirmada con éxito!",
                    onDismiss = onDismissConfirmationSuccessMessage
                )
            }
        }
    } else if (isPatient) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            if (loadingDoseConfirmation) {
                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {},
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = animatedContainerColor,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(25.dp), color = Color.White)
                }
            } else {
                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onConfirmation,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = animatedContainerColor,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(R.drawable.check_solid_full),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirmar dosis")
                }
            }
        }

        if (showConfirmationDialog) {
            DoseConfirmationDialog(
                title = "¿Confirmar dosis?",
                message = "¿Estás seguro de que deseas confirmar esta dosis? Esta acción quedará registrada.",
                confirmText = "Confirmar",
                dismissText = "Cancelar",
                isLoading = loadingDoseConfirmation,
                onConfirm = { observations ->
                    onConfirmationConfirmed(observations)
                },
                onDismiss = {
                    onConfirmationDismissed()
                }
            )
        }
    }
}
