package com.example.insulator

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.insulator.core.data.repositories.IUserRepository
import com.example.insulator.core.data.repositories.UserRepository
import com.example.insulator.core.data.repositories.CalculatorPreferencesRepository
import com.example.insulator.core.data.repositories.dataStore
import com.example.insulator.core.data.source.FoodDataSource
import com.example.insulator.core.services.AuthService
import com.example.insulator.core.services.IAuthService
import com.example.insulator.core.viewModels.AuthenticationViewModel
import com.example.insulator.core.viewModels.InsulatorCalculationViewModel
import com.example.insulator.core.viewModels.MainViewModel
import io.ktor.client.HttpClient as KtorHttpClient
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
    viewModelOf(::MainViewModel)
    viewModelOf(::InsulatorCalculationViewModel)
    viewModelOf(::AuthenticationViewModel)
    //Repositories
    singleOf(::UserRepository) bind IUserRepository::class
    singleOf(::CalculatorPreferencesRepository)
    //Services
    singleOf(::AuthService) bind IAuthService::class
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