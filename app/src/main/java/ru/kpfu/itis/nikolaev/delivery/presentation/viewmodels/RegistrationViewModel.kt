package ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignInModel
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignUpModel
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.SignInUseCase
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.SignUpUseCase

class RegistrationViewModel: ViewModel() {

    private var _currentUserFlow = MutableSharedFlow<Boolean>(1)
    val currentUserFlow: SharedFlow<Boolean?>
        get() = _currentUserFlow
    fun signUpWithEmailAndPassword(user: UserSignUpModel) {
        viewModelScope.launch {
            try {
                val result = SignUpUseCase(Dispatchers.IO).invoke(user)
                _currentUserFlow.emit(result)
            } catch (e: Exception) {
                Log.e("TAG_error", "error")
            }
        }
    }
}