package com.example.insulator.core.services.user

import com.example.insulator.BuildConfig
import com.example.insulator.core.data.dto.user.AssignUserAsPatientRequest
import com.example.insulator.core.data.dto.user.AssignableUserResponse
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

class UserService(
    private val client: HttpClient, private val tokenRepository: TokenRepository
) {
    //USED BY MEDICS
    suspend fun checkAvailability(email: String): Result<AssignableUserResponse?> {
        //TODO : Implement Ktor Interceptors for tokens
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.get(urlString = BuildConfig.WORKER_URL + BuildConfig.MEDICS_ROUTE + "/user/availability") {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                    parameter("email", email)
                }
            if (response.status.isSuccess()) {
                val responseList: List<AssignableUserResponse> = response.body()
                val user = responseList.firstOrNull()
                return Result.success(user)
            } else {
                Result.failure(Exception("Error al obtener usuario"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    suspend fun assignUserAsAPatient(id: String, hasBeenPatient: Boolean): Result<Any> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.post(urlString = BuildConfig.WORKER_URL + BuildConfig.MEDICS_ROUTE + "/user/assign") {
                    contentType(ContentType.Application.Json)
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                    setBody(
                        AssignUserAsPatientRequest(
                            userId = id,
                            hasBeenPatient = hasBeenPatient
                        )
                    )
                }
            if (response.status.isSuccess()) {
                return Result.success(true)
            } else {
                Result.failure(Exception("Error al asignar usuario como paciente"))
            }
        } catch (e: Exception) {
            Result.failure(Exception())
        }
    }
}

