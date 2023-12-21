package com.dicoding.basuwara.ui.screen.LoginPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.basuwara.data.repository.AuthRepository
import com.dicoding.basuwara.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _loginState = Channel<LoginState>()
    val loginState = _loginState.receiveAsFlow()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            repository.loginUser(email, password).collect {result ->
                when(result) {
                    is Result.Success -> _loginState.send(LoginState(isSuccess = result.data?.user?.uid))
                    is Result.Loading -> _loginState.send(LoginState(isLoading = true))
                    is Result.Error -> _loginState.send(LoginState(isError = result.message))
                }
            }
        }
    }

    fun saveSession(id: String) {
        viewModelScope.launch {
            repository.saveSession(id)
        }
    }
}