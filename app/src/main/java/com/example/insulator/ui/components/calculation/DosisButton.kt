package com.example.insulator.ui.components.calculation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.insulator.R

@Composable
fun DosisCalculation(handler: () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = handler,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
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

    }
}
