package com.example.socialcomposeapp.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _navigationState = MutableSharedFlow<SignUpNavigationEvent>()
    val navigationState = _navigationState.asSharedFlow()

    private val uiEvent = MutableStateFlow<SignUpEvent>(SignUpEvent.Normal)
    val uiState = uiEvent.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _buttonEnabled = MutableStateFlow(false)
    val buttonEnabled = _buttonEnabled.asStateFlow()

    private fun validate() {
        _buttonEnabled.value = email.value.isNotEmpty() &&
                password.value.isNotEmpty() &&
                name.value.isNotEmpty()
    }

    fun onEmailChange(email: String) {
        _email.value = email
        validate()
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        validate()
    }

    fun onNameChange(name: String) {
        _name.value = name
        validate()
    }

    fun signUp() {
        uiEvent.value = SignUpEvent.Loading
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val user = mapOf(
                            "userId" to userId,
                            "name" to name.value,
                            "email" to email.value
                        )

                        firestore.collection("users").document(userId).set(user)
                            .addOnSuccessListener {
                                uiEvent.value = SignUpEvent.Success
                                viewModelScope.launch {
                                    _navigationState.emit(SignUpNavigationEvent.NavigateToHome)
                                }
                            }
                            .addOnFailureListener { exception ->
                                uiEvent.value = SignUpEvent.Error(exception.message ?: "Error storing data")
                            }
                    } else {
                        uiEvent.value = SignUpEvent.Error("User ID not found")
                    }
                } else {
                    uiEvent.value = SignUpEvent.Error(result.exception?.message ?: "Unknown error")
                }
            }
    }
    fun onLoginButtonClicked() {
        viewModelScope.launch {
            _navigationState.emit(SignUpNavigationEvent.NavigateToLogin)
        }
    }
}

sealed class SignUpNavigationEvent {
    object NavigateToLogin : SignUpNavigationEvent()
    object NavigateToHome : SignUpNavigationEvent()
}

sealed class SignUpEvent {
    object Normal : SignUpEvent()
    object Loading : SignUpEvent()
    data class Error(val message: String) : SignUpEvent()
    object Success : SignUpEvent()
}