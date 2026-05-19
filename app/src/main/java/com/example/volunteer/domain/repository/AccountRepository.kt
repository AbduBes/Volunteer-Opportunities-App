package com.example.volunteer.domain.repository

sealed interface AuthResult {
    data class Success(val accountId: Int) : AuthResult
    data class Failure(val message: String) : AuthResult
}

interface AccountRepository {
    /** Creates an account plus a matching user profile. */
    suspend fun register(name: String, email: String, password: String): AuthResult

    /** Verifies credentials and returns the account id on success. */
    suspend fun login(email: String, password: String): AuthResult
}
