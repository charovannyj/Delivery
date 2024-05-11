package ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignInModel
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.SignInUseCase

class AuthViewModel : ViewModel() {

    private var _currentUserFlow = MutableSharedFlow<Boolean>(1)
    val currentUserFlow: SharedFlow<Boolean?>
        get() = _currentUserFlow

    fun signInWithEmailAndPassword(user: UserSignInModel) {
        viewModelScope.launch {
            try {
                val result = SignInUseCase(Dispatchers.IO).invoke(user)
                _currentUserFlow.emit(result)
            } catch (e: Exception) {
                Log.e("TAG_error", "error")
            }
        }
    }
}