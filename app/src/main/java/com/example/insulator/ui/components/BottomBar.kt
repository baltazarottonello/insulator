package com.example.insulator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.insulator.R
import com.example.insulator.ui.routes.AppRoutes

@Composable
fun InsulatorBottomBar(
    currentRoute: String?,
    roles: List<String>,
    onNavigateToManagement: () -> Unit
) {
    if (currentRoute != AppRoutes.Authentication.name) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            contentPadding = PaddingValues(0.dp),
            actions = {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        //TODO: use an enum or interface
                        if (roles.contains("ADMINISTRADOR")) {
                            BottomAppBarCustomNavButton(
                                text = "Gestión",
                                resId = R.drawable.users_solid_full,
                                handler = onNavigateToManagement
                            )
                        } else if (roles.contains("MÉDICO")) {
                            BottomAppBarCustomNavButton(
                                text = "Gestión",
                                resId = R.drawable.users_solid_full,
                                handler = onNavigateToManagement
                            )
                        } else {
                            null
                        }
                    }
                }
            }
        )
    } else null
}