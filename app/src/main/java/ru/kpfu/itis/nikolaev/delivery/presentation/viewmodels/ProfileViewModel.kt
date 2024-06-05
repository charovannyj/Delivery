package ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserModel
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignInModel
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.FetchProfileDataUseCase
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.GetUserDataUseCase
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.SignInUseCase

class ProfileViewModel: ViewModel() {

    private var _currentUserDataFlow = MutableSharedFlow<UserModel>(1)
    val currentUserDataFlow: SharedFlow<UserModel?>
        get() = _currentUserDataFlow
    fun getUserData(uid: String){
        viewModelScope.launch {
            try{
                val result = GetUserDataUseCase(Dispatchers.IO).invoke(uid)
                _currentUserDataFlow.emit(result)
            } catch (e: Exception) {
                Log.e("TAG_error", "error")
            }
        }
    }
}