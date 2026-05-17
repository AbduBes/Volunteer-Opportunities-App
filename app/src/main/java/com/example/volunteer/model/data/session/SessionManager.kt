package com.example.volunteer.model.data.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Holds the currently signed-in account id for the lifetime of the process only.
 * It is not persisted, so the user must sign in again after every app launch.
 */
@Singleton
class SessionManager @Inject constructor() {

    private val _currentAccountId = MutableStateFlow<Int?>(null)
    val currentAccountId: StateFlow<Int?> = _currentAccountId.asStateFlow()

    fun setLoggedIn(accountId: Int) {
        _currentAccountId.value = accountId
    }

    fun clear() {
        _currentAccountId.value = null
    }
}
