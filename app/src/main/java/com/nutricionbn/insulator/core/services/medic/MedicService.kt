package com.nutricionbn.insulator.core.services.medic

import com.nutricionbn.insulator.BuildConfig
import com.nutricionbn.insulator.core.data.dto.medic.PatientMedicCard
import com.nutricionbn.insulator.core.data.dto.medic.UnlinkMedicRequest
import com.nutricionbn.insulator.core.data.repositories.tokens.TokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.firstOrNull

class MedicService(private val client: HttpClient, private val tokenRepository: TokenRepository) {

    suspend fun getMedics(): Result<List<PatientMedicCard>> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.get(urlString = BuildConfig.WORKER_URL + "/patient" + "/medics") {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                }
            if (response.status.isSuccess()) {
                val responseBody: List<PatientMedicCard> = response.body();
                Result.success(responseBody)
            } else {
                Result.failure(Exception("Error al obtener médicos"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red"))
        }
    }

    suspend fun unlinkMedic(request: UnlinkMedicRequest): Result<Any> {
        return try {
            val accessToken = tokenRepository.accessToken.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                return Result.failure(Exception("No se encontró un token de sesión válido"))
            }
            val response: HttpResponse =
                client.put(urlString = BuildConfig.WORKER_URL + "/patient" + "/medics" + "/unlink") {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    header("Authorization", "Bearer $accessToken")
                    parameter("id", request.id)
                }
            if (response.status.isSuccess()) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error al desvincular médico"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red"))
        }
    }
}