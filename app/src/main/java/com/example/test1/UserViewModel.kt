package com.example.test1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.model.User
import com.example.test1.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UserUiState {
    data object Loading : UserUiState
    data class Success(val users: List<User>) : UserUiState
    data class Error(val message: String) : UserUiState
}

class UserViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private var isFetching = false
    private var isLastPage = false
    private val userList = mutableListOf<User>()

    init {
        fetchUsers(isRefresh = true)
    }

    fun loadMoreUsers() {
        if (!isFetching && !isLastPage) {
            fetchUsers()
        }
    }

    fun refresh() {
        fetchUsers(isRefresh = true)
    }

    private fun fetchUsers(isRefresh: Boolean = false) {
        if (isFetching) return

        isFetching = true
        if (isRefresh) {
            currentPage = 1
            isLastPage = false
            userList.clear()
            _uiState.value = UserUiState.Loading
        }

        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getUsers(page = currentPage, perPage = 6)
                if (response.isSuccessful) {
                    val body = response.body()
                    val newUsers = body?.data ?: emptyList()
                    if (newUsers.isEmpty()) {
                        isLastPage = true
                    }
                    userList.addAll(newUsers)
                    _uiState.value = UserUiState.Success(ArrayList(userList))
                    currentPage++
                } else {
                    _uiState.value = UserUiState.Error("API Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error("Network Error: ${e.message}")
            } finally {
                isFetching = false
            }
        }
    }
}