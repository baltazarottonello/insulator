package com.nutricionbn.insulator.ui.components.management.patients

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nutricionbn.insulator.R
import com.nutricionbn.insulator.core.data.dto.patient.PatientDoseHistoryRow
import com.nutricionbn.insulator.ui.components.profile.InformationDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DoseHistoryRowItem(
    item: PatientDoseHistoryRow,
) {
    val inputDate =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", LocalLocale.current.platformLocale)
    val outputDate = SimpleDateFormat("dd/MM/yyyy HH:mm", LocalLocale.current.platformLocale)

    val calendar = Calendar.getInstance()
    val dateString = try {
        val date = inputDate.parse(item.createdAt)
        if (date != null) {
            calendar.time = date
        }
        outputDate.format(date!!)
    } catch (e: Exception) {
        null
    }

    val mealTag = if (dateString != null) {
        getMealTag(calendar.get(Calendar.HOUR_OF_DAY))
    } else {
        "Desconocido"
    }

    val formattedDose = String.format(Locale.US, "%.1f", item.dose)
    val formattedCorrection = String.format(Locale.US, "%.1f", item.doseCorrection)
    val formattedFoodDose = String.format(Locale.US, "%.1f", item.foodDose)

    var showObservationsModal by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.dp,
            brush = androidx.compose.ui.graphics.SolidColor(
                MaterialTheme.colorScheme.outline.copy(
                    alpha = 0.5f
                )
            )
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateString ?: "Fecha inválida",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                MealTagBadge(tag = mealTag)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Dosis total: $formattedDose U",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Glucosa",
                    value = "${item.bloodGlucose.toInt()} mg/dL"
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Carbohidratos",
                    value = "${item.carbs.toInt()} g"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Ratio",
                    value = item.ratio.toString()
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Sensibilidad",
                    value = item.sensitivity.toString()
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Objetivo",
                    value = "${item.targetGlucose.toInt()}"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Corrección",
                    value = "$formattedCorrection U"
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Dosis Comida",
                    value = "$formattedFoodDose U"
                )
            }
            if (item.observations != null) {
                Spacer(Modifier.height(8.dp))
                ElevatedButton(
                    onClick = { showObservationsModal = true },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(R.drawable.comment_dots_solid_full),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Observaciones")
                    }
                }
            }

            if (showObservationsModal) {
                InformationDialog(
                    title = "Observaciones",
                    message = item.observations!!,
                    onDismiss = { showObservationsModal = false },
                    buttonText = "Volver"
                )
            }
        }
    }
}

@Composable
fun MetricCard(modifier: Modifier = Modifier, label: String, value: String) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun getMealTag(hourOfDay: Int): String {
    return when (hourOfDay) {
        in 5..10 -> "Desayuno"
        in 11..12 -> "Colación media mañana"
        in 13..15 -> "Almuerzo"
        in 16..18 -> "Merienda"
        in 19..20 -> "Colación media tarde"
        else -> "Cena"
    }
}

@Composable
fun MealTagBadge(tag: String) {
    val colors = getTagColors(tag)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(colors.background)
            .border(
                width = 1.dp,
                color = colors.border,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall,
            color = colors.text,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class TagColorStyle(
    val background: Color,
    val border: Color,
    val text: Color
)

private fun getTagColors(tag: String): TagColorStyle {
    return when (tag) {
        "Desayuno" -> TagColorStyle(
            background = Color(0xFFE0F2FE),
            border = Color(0xFF38BDF8),
            text = Color(0xFF0369A1)
        )

        "Colación media mañana", "Almuerzo" -> TagColorStyle(
            background = Color(0xFFFEF9C3),
            border = Color(0xFFFACC15),
            text = Color(0xFF854D0E)
        )

        "Merienda", "Colación media tarde" -> TagColorStyle(
            background = Color(0xFFFFEDD5),
            border = Color(0xFFFB923C),
            text = Color(0xFF9A3412)
        )

        else -> TagColorStyle(
            background = Color(0xFFE0E7FF),
            border = Color(0xFF818CF8),
            text = Color(0xFF3730A3)
        )
    }
}