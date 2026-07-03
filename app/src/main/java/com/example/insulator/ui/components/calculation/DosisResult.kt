package com.example.insulator.ui.components.calculation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.insulator.R
import com.example.insulator.ui.theme.WarningColor
import java.util.Locale

@Composable
fun DosisResult(
    dosis: Float,
    correction: Float,
    food: Float,
    formValidationError: Boolean,
    paramsError: String
) {
    AnimatedContent(
        targetState = formValidationError,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "DosisErrorFallbackTransition"
    ) { error ->
        if (!error) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("RECOMENDACIÓN")
                    Text(
                        String.format(
                            Locale.US,
                            "%.1f",
                            dosis
                        ),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Unidades de insulina", fontWeight = FontWeight.SemiBold)

                    Spacer(modifier = Modifier.padding(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row {
                                Icon(
                                    modifier = Modifier.size(25.dp),
                                    painter = painterResource(R.drawable.utensils_solid_full),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Comida: ${String.format(Locale.US, "%.1f", food)} U")
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row {
                                Icon(
                                    modifier = Modifier.size(25.dp),
                                    painter = painterResource(R.drawable.droplet_solid_full),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Corrección: ${
                                        String.format(
                                            Locale.US,
                                            "%.1f",
                                            correction
                                        )
                                    } U"
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (paramsError.isEmpty()) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.circle_exclamation_solid_full),
                        contentDescription = null,
                        tint = WarningColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Hay campos requeridos incompletos",
                        color = WarningColor
                    )
                } else null
            }
        }
    }
}

