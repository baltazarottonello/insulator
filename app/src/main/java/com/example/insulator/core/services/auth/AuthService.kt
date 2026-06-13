package com.example.insulator.core.services.auth

import com.example.insulator.BuildConfig
import com.example.insulator.core.data.dto.auth.GoogleLoginRequest
import com.example.insulator.core.data.dto.auth.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthService(
    private val client: HttpClient
) : IAuthService {

    override suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse> {
        return try {
            val response: HttpResponse =
                client.post(urlString = BuildConfig.WORKER_URL + BuildConfig.AUTH_ROUTE) {
                    header("X-App", BuildConfig.X_APP_HEADER)
                    contentType(ContentType.Application.Json)
                    setBody(GoogleLoginRequest(token = idToken))
                }
            if (response.status.isSuccess()) {
                val responseBody: LoginResponse = response.body();
                Result.success(responseBody)
            } else {
                Result.failure(Exception("Error de servidor: código ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }
}