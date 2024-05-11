package ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.model.user.UserSignInModel
import ru.kpfu.itis.nikolaev.delivery.model.user.usecase.SignInUseCase

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