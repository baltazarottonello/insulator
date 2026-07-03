package com.example.insulator

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
import com.example.insulator.ui.components.InsulatorBottomBar
import com.example.insulator.ui.components.InsulatorTopAppBar
import com.example.insulator.ui.routes.AppRoutes
import com.example.insulator.ui.screens.AuthenticationScreen
import com.example.insulator.ui.screens.CalculationScreen
import com.example.insulator.ui.screens.ManagementScreen
import com.example.insulator.ui.screens.MyPatientsScreen
import com.example.insulator.ui.screens.PatientManagementScreen
import com.example.insulator.ui.screens.SearchPatientScreen
import com.example.insulator.ui.theme.InsulatorTheme
import com.example.insulator.ui.viewModels.MainViewModel
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
//                            navController.navigate(AppRoutes.Profile.name)
                        }
                    )
                }, bottomBar = {
                    if (userState.isLogged) {
                        InsulatorBottomBar(
                            currentRoute = currentRoute,
                            roles = userState.roles,
                            onNavigateToManagement = {
                                navController.navigate(
                                    AppRoutes.Management.name
                                )
                            })
                    } else null
                }) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = AppRoutes.Authentication.name,
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
                            composable(route = AppRoutes.Management.name) {
                                ManagementScreen(onNavigateToPatientsManagement = {
                                    navController.navigate(AppRoutes.PatientsManagement.name)
                                })
                            }
                            composable(route = AppRoutes.PatientsManagement.name) {
                                PatientManagementScreen(onNavigateToSearchPatientScreen = {
                                    navController.navigate(AppRoutes.SearchPatient.name)
                                }, onNavigateToMyPatientsScreen = {
                                    navController.navigate(AppRoutes.MyPatients.name)
                                })
                            }
                            composable (route = AppRoutes.SearchPatient.name){
                                SearchPatientScreen()
                            }
                            composable(route = AppRoutes.MyPatients.name){
                                MyPatientsScreen()
                            }
//                            composable(route = AppRoutes.Profile.name) {
//                                UserProfileScreen()
//                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun InsulatorPreview() {
//    KoinApplication(application = {
//        modules(module {
//
//            //Data Store
//            single<DataStore<Preferences>> { androidContext().dataStore }
//
//            //HTTP
//            single {
//                KtorHttpClient(Android) {
//                    install(ContentNegotiation) {
//                        json(Json {
//                            prettyPrint = true
//                            isLenient = true
//                            ignoreUnknownKeys = true
//                        })
//                    }
//                }
//            }
//
//            //viewModels
//            viewModelOf(::UserProfileViewModel)
//            viewModelOf(::MainViewModel)
//            viewModelOf(::InsulatorCalculationViewModel)
//            viewModelOf(::AuthenticationViewModel)
//            //Repositories
//            singleOf(::UserRepository) bind IUserRepository::class
//            singleOf(::CalculatorPreferencesRepository)
//            //Services
//            singleOf(::AuthService) bind IAuthService::class
//            //DataSource
//            singleOf(::FoodDataSource)
//        })
//    }) {
//        val viewModel: MainViewModel = koinViewModel()
//        val userState by viewModel.userState.collectAsStateWithLifecycle()
//        InsulatorTheme {
//            val navController = rememberNavController()
//            Scaffold(
//                modifier = Modifier.fillMaxSize(), topBar = {
//                    InsulatorTopAppBar(
//                        userName = userState.fullName,
//                        profileImageUrl = userState.avatar,
//                        onNavigateToProfile = {
//                            navController.navigate(AppRoutes.Profile.name)
//                        }
//                    )
//                }, bottomBar = {
//                    if (userState.isLogged) {
//                        InsulatorBottomBar()
//                    } else null
//                }) { innerPadding ->
//                Surface(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    NavHost(
//                        navController = navController,
//                        startDestination = AppRoutes.Authentication.name,
//                        modifier = Modifier.padding(innerPadding)
//                    ) {
//                        composable(route = AppRoutes.Authentication.name) {
//                            AuthenticationScreen(onNavigateToCalculation = {
//                                navController.navigate(AppRoutes.Calculation.name)
//                                {
//                                    popUpTo(
//                                        AppRoutes.Authentication.name
//                                    ) { inclusive = true }
//                                }
//                            })
//                        }
//                        composable(route = AppRoutes.Calculation.name) {
//                            CalculationScreen()
//                        }
//                        composable(route = AppRoutes.Profile.name) {
//                            UserProfileScreen()
//                        }
//                    }
//                }
//            }
//        }
//    }
//}