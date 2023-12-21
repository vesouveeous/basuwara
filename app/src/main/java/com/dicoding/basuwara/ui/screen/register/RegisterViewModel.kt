package com.dicoding.basuwara.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.basuwara.data.model.UserModel
import com.dicoding.basuwara.data.repository.AuthRepository
import com.dicoding.basuwara.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _registerState = Channel<RegisterState>()
    val registerState = _registerState.receiveAsFlow()

    fun registerUser(user: UserModel) {
        viewModelScope.launch {
            repository.registerUser(user).collect {
                when(it) {
                    is Result.Error -> _registerState.send(RegisterState(isError = it.message))
                    is Result.Success -> _registerState.send(RegisterState(isSuccess = true))
                    is Result.Loading -> _registerState.send(RegisterState(isLoading = true))
                }
            }
        }
    }
}