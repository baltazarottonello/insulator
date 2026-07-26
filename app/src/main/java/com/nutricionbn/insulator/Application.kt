package com.nutricionbn.insulator

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nutricionbn.insulator.core.data.repositories.calculation.CalculationSnapshotRepository
import com.nutricionbn.insulator.core.data.repositories.calculation.CalculatorPreferencesRepository
import com.nutricionbn.insulator.core.data.repositories.patients.PatientRepository
import com.nutricionbn.insulator.core.data.repositories.tokens.TokenRepository
import com.nutricionbn.insulator.core.data.repositories.tokens.authPrefsDataStore
import com.nutricionbn.insulator.core.data.repositories.user.IUserRepository
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
import com.nutricionbn.insulator.core.data.repositories.user.UserRepository
import com.nutricionbn.insulator.core.data.source.FoodDataSource
import com.nutricionbn.insulator.core.services.auth.AuthService
import com.nutricionbn.insulator.core.services.auth.IAuthService
import com.nutricionbn.insulator.core.services.patient.PatientService
import com.nutricionbn.insulator.core.services.user.UserService
import com.nutricionbn.insulator.ui.viewModels.MainViewModel
import com.nutricionbn.insulator.ui.viewModels.auth.AuthenticationViewModel
import com.nutricionbn.insulator.ui.viewModels.calculation.CalculationViewModel
import com.nutricionbn.insulator.ui.viewModels.management.ManagementViewModel
import com.nutricionbn.insulator.ui.viewModels.management.patients.MyPatientsViewModel
import com.nutricionbn.insulator.ui.viewModels.management.patients.SearchUserViewModel
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import io.ktor.client.HttpClient as KtorHttpClient

val appModule = module {

    //Data Store
    single<DataStore<Preferences>>(named("authPrefsDataStore")) { androidContext().authPrefsDataStore }

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
    //viewModelOf(::UserProfileViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::CalculationViewModel)
    viewModelOf(::ManagementViewModel)
    viewModelOf(::SearchUserViewModel)
    viewModelOf(::MyPatientsViewModel)
    //Repositories
    singleOf(::CalculationSnapshotRepository)
    singleOf(::CalculatorPreferencesRepository)
    single {
        TokenRepository(
            authPrefsDataStore = get(named("authPrefsDataStore"))
        )
    }
    singleOf(::UserRepository) bind IUserRepository::class
    singleOf(::PatientRepository)
    singleOf(::SessionRepository)
    //Services
    singleOf(::AuthService) bind IAuthService::class
    singleOf(::PatientService)
    singleOf(::UserService)
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