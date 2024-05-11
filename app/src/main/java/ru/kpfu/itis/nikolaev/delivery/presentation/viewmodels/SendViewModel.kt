package ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SendViewModel : ViewModel() {

    private val _currentFromFlow = MutableStateFlow<FirebaseUser?>(null)
    val currentFromFlow: StateFlow<FirebaseUser?> = _currentFromFlow.asStateFlow()

    private val _currentUserFlow = MutableStateFlow<FirebaseUser?>(null)
    val currentUserFlow: StateFlow<FirebaseUser?> = _currentUserFlow.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await().user
                _currentUserFlow.emit(user)
            } catch (e: Exception) {
                // Handle exceptions (e.g., emit an error state or log the error)
                _currentUserFlow.emit(null)
            }
        }
    }

}