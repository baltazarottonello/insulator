package com.nutricionbn.insulator.core.services.user

import com.nutricionbn.insulator.BuildConfig
import com.nutricionbn.insulator.core.data.dto.user.AssignUserAsPatientRequest
import com.nutricionbn.insulator.core.data.dto.user.AssignableUserResponse
import com.nutricionbn.insulator.core.data.dto.user.DeleteUserDataRequest
import com.nutricionbn.insulator.core.data.dto.user.DoseUploadRequest
import com.nutricionbn.insulator.core.data.dto.user.UpdateUserProfileDataRequest
import com.nutricionbn.insulator.core.data.repositories.tokens.TokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
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
            Result.failure(Exception("Error de red"))
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
            Result.failure(Exception("Error de conexión"))
        }
    }

    suspend fun uploadDose(doseData: DoseUploadRequest): Result<Any> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.post(urlString = BuildConfig.WORKER_URL + "/user" + "/dose") {
                    contentType(ContentType.Application.Json)
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                    setBody(doseData)
                }
            if (response.status.isSuccess()) {
                return Result.success(true)
            } else {
                Result.failure(Exception("Error al cargar dosis"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión"))
        }
    }

    suspend fun saveProfileData(data: UpdateUserProfileDataRequest): Result<Any> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.put(urlString = BuildConfig.WORKER_URL + "/user" + "/profile") {
                    contentType(ContentType.Application.Json)
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                    setBody(data)
                }
            if (response.status.isSuccess()) {
                return Result.success(true)
            } else {
                Result.failure(Exception("Error al actualizar datos"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión"))
        }
    }

    suspend fun deleteUserData(): Result<Any> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.delete(urlString = BuildConfig.WORKER_URL + "/user" + "/delete") {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                }
            if (response.status.isSuccess()) {
                return Result.success(true)
            } else {
                Result.failure(Exception("Error al eliminar datos"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión"))
        }
    }
}


