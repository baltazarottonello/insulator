package com.nutricionbn.insulator.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import com.nutricionbn.insulator.R
import com.nutricionbn.insulator.ui.routes.AppRoutes
import com.nutricionbn.insulator.ui.viewModels.UserState

@Composable
fun InsulatorBottomBar(
    currentRoute: String?,
    user: UserState,
    onNavigateToMedicManagement: () -> Unit,
    onNavigateToPatientManagement: () -> Unit,
    onNavigateToAdminManagement: () -> Unit,
    onNavigateToCalculation: () -> Unit
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
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //TODO: use an enum or interface
                        if (user.isAdmin) {
                            BottomAppBarCustomNavButton(
                                text = "Inicio",
                                resId = R.drawable.house_solid_full,
                                handler = onNavigateToCalculation
                            )
                            BottomAppBarCustomNavButton(
                                text = "Admin",
                                resId = R.drawable.user_tie_solid_full,
                                handler = onNavigateToAdminManagement
                            )
                            if (user.isMedic) {
                                BottomAppBarCustomNavButton(
                                    text = "Gestión",
                                    resId = R.drawable.users_solid_full,
                                    handler = onNavigateToMedicManagement
                                )
                            }
                        } else if (user.isMedic) {
                            BottomAppBarCustomNavButton(
                                text = "Inicio",
                                resId = R.drawable.house_solid_full,
                                handler = onNavigateToCalculation
                            )
                            BottomAppBarCustomNavButton(
                                text = "Gestión",
                                resId = R.drawable.users_solid_full,
                                handler = onNavigateToMedicManagement
                            )
                        } else if (user.isPatient) {
                            BottomAppBarCustomNavButton(
                                text = "Inicio",
                                resId = R.drawable.house_solid_full,
                                handler = onNavigateToCalculation
                            )
                            BottomAppBarCustomNavButton(
                                text = "Gestión",
                                resId = R.drawable.users_solid_full,
                                handler = onNavigateToPatientManagement
                            )
                        } else null
                    }
                }
            }
        )
    } else null
}