package com.dicoding.basuwara.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.basuwara.data.repository.AuthRepository
import com.dicoding.basuwara.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _homeState = Channel<HomeState>()
    val homeState = _homeState.receiveAsFlow()

    private val _idState = MutableStateFlow("")
    val idState: Flow<String> = _idState

    fun getUserId(){
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isNotEmpty()){
                    _idState.value = it
                }
            }
        }
    }
    fun getUserInfo(userId: String) {
        viewModelScope.launch {
            repository.getUserDataFromDatabase(userId).collect {
                when(it) {
                    is Result.Success -> _homeState.send(HomeState(isSuccess = it.data, isLoading = false))
                    is Result.Loading -> _homeState.send(HomeState(isLoading = true))
                    is Result.Error -> _homeState.send(HomeState(isError = it.message))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}