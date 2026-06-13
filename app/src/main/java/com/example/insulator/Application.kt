package com.example.insulator

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.insulator.core.data.repositories.calculation.CalculatorPreferencesRepository
import com.example.insulator.core.data.repositories.calculation.dataStore
import com.example.insulator.core.data.repositories.patients.PatientRepository
import com.example.insulator.core.data.repositories.tokens.TokenRepository
import com.example.insulator.core.data.repositories.user.IUserRepository
import com.example.insulator.core.data.repositories.user.UserRepository
import com.example.insulator.core.data.source.FoodDataSource
import com.example.insulator.core.services.auth.AuthService
import com.example.insulator.core.services.auth.IAuthService
import com.example.insulator.core.services.patient.PatientService
import com.example.insulator.ui.viewModels.MainViewModel
import com.example.insulator.ui.viewModels.auth.AuthenticationViewModel
import com.example.insulator.ui.viewModels.calculation.InsulatorCalculationViewModel
import com.example.insulator.ui.viewModels.management.ManagementViewModel
import com.example.insulator.ui.viewModels.management.patients.PatientManagementViewModel
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import io.ktor.client.HttpClient as KtorHttpClient

val appModule = module {

    //Data Store
    single<DataStore<Preferences>> { androidContext().dataStore }

    //HTTP
    single {
        KtorHttpClient(Android) {
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
//    viewModelOf(::UserProfileViewModel)
    viewModelOf(::PatientManagementViewModel)
    viewModelOf(::ManagementViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::InsulatorCalculationViewModel)
    viewModelOf(::AuthenticationViewModel)
    //Repositories
    singleOf(::TokenRepository)
    singleOf(::PatientRepository)
    singleOf(::UserRepository) bind IUserRepository::class
    singleOf(::CalculatorPreferencesRepository)
    //Services
    singleOf(::AuthService) bind IAuthService::class
    singleOf(::PatientService)
    //DataSource
    singleOf(::FoodDataSource)
}

class InsulatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@InsulatorApplication)
            modules(appModule)
        }
    }
}