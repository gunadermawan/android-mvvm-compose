package com.gunder.mvvm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunder.mvvm.data.remote.User
import com.gunder.mvvm.data.remote.UserServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userServices: UserServices) : ViewModel() {
    val userState = MutableStateFlow<UserState>(UserState.StartState)

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            userState.tryEmit(UserState.StartState)
            try {
                val users = userServices.fetchUser()
                userState.tryEmit(UserState.Success(users))
            } catch (e: Exception) {
                userState.tryEmit(UserState.Error(e.localizedMessage))
            }
        }
    }
}

sealed class UserState {
    object StartState : UserState()
    object LoadingState : UserState()
    data class Success(val users: List<User>) : UserState()
    data class Error(val errorMessage: String) : UserState()
}