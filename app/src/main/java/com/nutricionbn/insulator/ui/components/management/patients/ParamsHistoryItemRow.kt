package com.nutricionbn.insulator.ui.components.management.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nutricionbn.insulator.core.data.dto.patient.PatientParamsHistoryRow
import java.text.SimpleDateFormat

@Composable
fun ParamsHistoryRowItem(item: PatientParamsHistoryRow) {
    val inputDate =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", LocalLocale.current.platformLocale)
    val outputDate = SimpleDateFormat("dd/MM/yyyy HH:mm", LocalLocale.current.platformLocale)

    val dateString = try {
        val date = inputDate.parse(item.createdAt)
        outputDate.format(date!!)
    } catch (e: Exception) {
        "Fecha inválida"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isActive) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Fecha: $dateString",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricColumn("Ratio", item.ratio.toString())
                MetricColumn("Sensibilidad", item.sensitivity.toString())
                MetricColumn("Glucosa objetivo", item.targetGlucose.toString())
            }
        }
    }
}

@Composable
fun MetricColumn(label: String, value: String) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}