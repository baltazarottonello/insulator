package com.example.insulator.core.services.patient

import com.example.insulator.BuildConfig
import com.example.insulator.core.data.dto.patient.ParametersResponse
import com.example.insulator.core.data.dto.patient.PatientCard
import com.example.insulator.core.data.dto.patient.PatientParameters
import com.example.insulator.core.data.dto.patient.PatientParamsHistoryResponse
import com.example.insulator.core.data.dto.patient.PatientResponse
import com.example.insulator.core.data.dto.patient.SetParametersRequest
import com.example.insulator.core.data.repositories.tokens.TokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.firstOrNull

class PatientService(
    private val client: HttpClient, private val tokenRepository: TokenRepository
) {
    //USED BY MEDICS
    suspend fun searchPatientByEmail(email: String): Result<PatientResponse> {
        //TODO : Implement Ktor Interceptors for tokens
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.get(urlString = BuildConfig.WORKER_URL + BuildConfig.PATIENTS_ROUTE) {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                    parameter("email", email)
                }
            if (response.status.isSuccess()) {
                val responseBody: PatientResponse = response.body();
                Result.success(responseBody)
            } else {
                Result.failure(Exception("Error al obtener pacientes, paciente no asignado o no existe"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    suspend fun getPatients(id: String): Result<List<PatientCard>> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.get(urlString = BuildConfig.WORKER_URL + BuildConfig.MEDICS_ROUTE + "/patients") {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                    parameter("id", id)
                }
            if (response.status.isSuccess()) {
                val responseBody: List<PatientCard> = response.body();
                Result.success(responseBody)
            } else {
                Result.failure(Exception("Error al obtener pacientes, paciente no asignado o no existe"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    //USED BY PATIENTS
    suspend fun getParameters(): Result<ParametersResponse> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.get(urlString = BuildConfig.WORKER_URL + BuildConfig.PATIENTS_ROUTE + "/params") {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                }
            if (response.status.isSuccess()) {
                val responseBody: ParametersResponse = response.body();
                Result.success(responseBody)
            } else {
                Result.failure(Exception("Error al obtener parámetros del paciente"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    suspend fun setParameters(id: String, params: PatientParameters): Result<Any> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }

            val requestBody = SetParametersRequest(
                patientId = id,
                params = PatientParameters(
                    ratio = params.ratio,
                    sens = params.sens,
                    targetGlucose = params.targetGlucose
                )
            )
            val response: HttpResponse =
                client.post(urlString = BuildConfig.WORKER_URL + BuildConfig.PATIENTS_ROUTE + "/params") {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al obtener parámetros del paciente"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    suspend fun getHistory(id: String): Result<PatientParamsHistoryResponse> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.get(urlString = BuildConfig.WORKER_URL + BuildConfig.MEDICS_ROUTE + "/patient" + "/params-history") {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                    parameter("id", id)
                }
            if (response.status.isSuccess()) {
                val responseBody: PatientParamsHistoryResponse = response.body();
                Result.success(responseBody)
            } else {
                Result.failure(Exception("Error al obtener parámetros del paciente"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }
}