package com.example.insulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.insulator.core.data.repositories.IUserRepository
import com.example.insulator.core.data.repositories.UserRepository
import com.example.insulator.core.data.source.FoodDataSource
import com.example.insulator.core.services.AuthService
import com.example.insulator.core.services.IAuthService
import com.example.insulator.core.viewModels.AuthenticationViewModel
import com.example.insulator.core.viewModels.InsulatorCalculationViewModel
import com.example.insulator.core.viewModels.MainViewModel
import com.example.insulator.ui.components.InsulatorTopAppBar
import com.example.insulator.ui.routes.AppRoutes
import com.example.insulator.ui.screens.AuthenticationScreen
import com.example.insulator.ui.screens.CalculationScreen
import com.example.insulator.ui.theme.InsulatorTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = koinViewModel()
            val userState by viewModel.userState.collectAsStateWithLifecycle()
            InsulatorTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    InsulatorTopAppBar(
                        userName = userState.fullName,
                        profileImageUrl = userState.avatar
                    )
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
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InsulatorPreview() {
    KoinApplication(application = {
        modules(module {

            //HTTP
            single {
                HttpClient(Android) {
                    install(ContentNegotiation) {
                        json(Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        })
                    }
                }
            }

            //viewModels
            viewModelOf(::MainViewModel)
            viewModelOf(::InsulatorCalculationViewModel)
            viewModelOf(::AuthenticationViewModel)
            //Repositories
            singleOf(::UserRepository) bind IUserRepository::class
            //Services
            singleOf(::AuthService) bind IAuthService::class
            //DataSource
            singleOf(::FoodDataSource)
        })
    }) {
        InsulatorTheme {
            val navController = rememberNavController()
            val viewModel: MainViewModel = koinViewModel()
            val userState by viewModel.userState.collectAsStateWithLifecycle()
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                InsulatorTopAppBar(
                    userName = userState.fullName,
                    profileImageUrl = userState.avatar
                )
            }) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.Authentication.name,
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = {
                            fadeIn(animationSpec = tween(700))
                        },
                        exitTransition = {
                            fadeOut(animationSpec = tween(700))
                        }
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
                    }
                }
            }
        }
    }
}