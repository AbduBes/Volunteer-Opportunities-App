package com.example.volunteer.model.repository

import com.example.volunteer.model.data.local.dao.AccountDao
import com.example.volunteer.model.data.local.dao.UserProfileDao
import com.example.volunteer.model.data.local.entity.AccountEntity
import com.example.volunteer.model.data.local.entity.UserProfileEntity
import com.example.volunteer.model.data.local.util.PasswordHasher
import com.example.volunteer.domain.repository.AccountRepository
import com.example.volunteer.domain.repository.AuthResult
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val userProfileDao: UserProfileDao
) : AccountRepository {

    override suspend fun register(name: String, email: String, password: String): AuthResult {
        val normalizedEmail = email.trim().lowercase()
        if (accountDao.countByEmail(normalizedEmail) > 0) {
            return AuthResult.Failure("An account with this email already exists.")
        }
        val salt = PasswordHasher.newSalt()
        val accountId = accountDao.insert(
            AccountEntity(
                email = normalizedEmail,
                passwordHash = PasswordHasher.hash(password, salt),
                salt = salt,
                name = name.trim()
            )
        ).toInt()
        userProfileDao.insert(
            UserProfileEntity(
                userId = accountId,
                name = name.trim(),
                email = normalizedEmail,
                bio = "Passionate about helping the community.",
                totalHours = 0,
                questsCompleted = 0,
                streak = 0
            )
        )
        return AuthResult.Success(accountId)
    }

    override suspend fun login(email: String, password: String): AuthResult {
        val normalizedEmail = email.trim().lowercase()
        val account = accountDao.getByEmail(normalizedEmail)
            ?: return AuthResult.Failure("No account found with this email.")
        return if (PasswordHasher.verify(password, account.salt, account.passwordHash)) {
            AuthResult.Success(account.accountId)
        } else {
            AuthResult.Failure("Incorrect password.")
        }
    }
}
