package com.example.volunteer.model.data.local.util

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Salted password hashing with PBKDF2-HMAC-SHA256. No raw password is ever stored.
 * Iteration count is kept modest so it stays responsive on an emulator.
 */
object PasswordHasher {

    private const val ITERATIONS = 10_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_BYTES = 16

    /** Generates a fresh random salt, Base64-encoded. */
    fun newSalt(): String {
        val salt = ByteArray(SALT_BYTES)
        SecureRandom().nextBytes(salt)
        return Base64.encodeToString(salt, Base64.NO_WRAP)
    }

    /** Derives a Base64-encoded hash of [password] using [saltBase64]. */
    fun hash(password: String, saltBase64: String): String {
        val salt = Base64.decode(saltBase64, Base64.NO_WRAP)
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val derived = factory.generateSecret(spec).encoded
        return Base64.encodeToString(derived, Base64.NO_WRAP)
    }

    /** Constant-time check of [password] against a stored salt/hash pair. */
    fun verify(password: String, saltBase64: String, expectedHash: String): Boolean {
        val actual = hash(password, saltBase64)
        return MessageDigest.isEqual(
            actual.toByteArray(Charsets.UTF_8),
            expectedHash.toByteArray(Charsets.UTF_8)
        )
    }
}
