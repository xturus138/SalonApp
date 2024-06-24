// RegisterViewModel.kt
package com.example.comfestsea16.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _registerResult = MutableLiveData<FirebaseUser?>()
    val registerResult: LiveData<FirebaseUser?> = _registerResult

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val user = authRepository.register(email, password)
            _registerResult.postValue(user)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }
}
