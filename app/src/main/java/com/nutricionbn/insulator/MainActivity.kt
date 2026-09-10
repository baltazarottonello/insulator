package com.nutricionbn.insulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nutricionbn.insulator.ui.components.InsulatorBottomBar
import com.nutricionbn.insulator.ui.components.InsulatorTopAppBar
import com.nutricionbn.insulator.ui.routes.AppRoutes
import com.nutricionbn.insulator.ui.screens.AuthenticationScreen
import com.nutricionbn.insulator.ui.screens.CalculationScreen
import com.nutricionbn.insulator.ui.screens.UserProfileScreen
import com.nutricionbn.insulator.ui.screens.management.medic.MedicManagementScreen
import com.nutricionbn.insulator.ui.screens.management.medic.MedicPatientAssignmentScreen
import com.nutricionbn.insulator.ui.screens.management.medic.MedicPatientManagementScreen
import com.nutricionbn.insulator.ui.screens.management.medic.MedicPatientsScreen
import com.nutricionbn.insulator.ui.screens.management.patient.PatientManagementScreen
import com.nutricionbn.insulator.ui.screens.management.patient.PatientMedicAssignmentScreen
import com.nutricionbn.insulator.ui.screens.management.patient.PatientMedicManagementScreen
import com.nutricionbn.insulator.ui.theme.InsulatorTheme
import com.nutricionbn.insulator.ui.viewModels.MainViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = koinViewModel()
            val userState by viewModel.userState.collectAsStateWithLifecycle()
            InsulatorTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    InsulatorTopAppBar(
                        currentRoute = currentRoute,
                        userName = userState.fullName,
                        profileImageUrl = userState.avatar,
                        onNavigateToProfile = {
                            navController.navigate(AppRoutes.Profile.name)
                        }
                    )
                }, bottomBar = {
                    if (userState.isLogged) {
                        InsulatorBottomBar(
                            currentRoute = currentRoute,
                            user = userState,
                            onNavigateToMedicManagement = {
                                navController.navigate(
                                    AppRoutes.MedicManagementDashboard.name
                                )
                            },
                            onNavigateToPatientManagement = {
                                navController.navigate(
                                    AppRoutes.PatientManagementDashboard.name
                                )
                            },
                            onNavigateToAdminManagement = {},
                            onNavigateToCalculation = {
                                navController.navigate((AppRoutes.Calculation.name))
                            })
                    }
                }) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = if (userState.isLogged) AppRoutes.Calculation.name else AppRoutes.Authentication.name,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = AppRoutes.Authentication.name) {
                                AuthenticationScreen(onNavigateToCalculation = {
                                    navController.navigate(AppRoutes.Calculation.name)
                                    {
                                        popUpTo(
                                            AppRoutes.Authentication.name
                                        ) { inclusive = true }
                                    }
                                })
                            }
                            composable(route = AppRoutes.Calculation.name) {
                                CalculationScreen()
                            }
                            // MEDIC MANAGEMENT
                            composable(route = AppRoutes.MedicManagementDashboard.name) {
                                MedicManagementScreen(onNavigateToPatientsManagementScreen = {
                                    navController.navigate(AppRoutes.MedicPatientsManagement.name)
                                })
                            }
                            composable(route = AppRoutes.MedicPatientsManagement.name) {
                                MedicPatientManagementScreen(onNavigateToPatientAssignmentScreen = {
                                    navController.navigate(AppRoutes.MedicPatientAssignment.name)
                                }, onNavigateToMyPatientsScreen = {
                                    navController.navigate(AppRoutes.MedicPatients.name)
                                })
                            }
                            composable(route = AppRoutes.MedicPatientAssignment.name) {
                                MedicPatientAssignmentScreen()
                            }
                            composable(route = AppRoutes.MedicPatients.name) {
                                MedicPatientsScreen()
                            }
                            // PATIENT MANAGEMENT
                            composable(route = AppRoutes.PatientManagementDashboard.name) {
                                PatientManagementScreen(onNavigateToMedicsManagement = {
                                    navController.navigate(AppRoutes.PatientMedicsManagement.name)
                                })
                            }
                            composable(route = AppRoutes.PatientMedicsManagement.name) {
                                PatientMedicManagementScreen(onNavigateToMedicAssignmentScreen = {
                                    navController.navigate(AppRoutes.PatientMedicsAssignment.name)
                                })
                            }
                            composable(route = AppRoutes.PatientMedicsAssignment.name) {
                                PatientMedicAssignmentScreen(onNavigateAway = {
                                    navController.navigate(AppRoutes.Calculation.name) {
                                        popUpTo(AppRoutes.Calculation.name) { inclusive = true }
                                    }
                                })
                            }
                            //PROFILE
                            composable(route = AppRoutes.Profile.name) {
                                UserProfileScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}