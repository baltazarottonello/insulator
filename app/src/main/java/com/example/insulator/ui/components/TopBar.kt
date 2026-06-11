package com.example.insulator.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.insulator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsulatorTopAppBar(userName: String, profileImageUrl: String) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(R.drawable.calculator_solid_full),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold
                )
            }
        }, actions = {
            if (profileImageUrl != "") {
                IconButton(onClick = {}) {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Perfil de usuario", onError = {
                            Log.e("Coil", "Error cargando imagen", it.result.throwable)
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            } else null
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary
        )
    )
}