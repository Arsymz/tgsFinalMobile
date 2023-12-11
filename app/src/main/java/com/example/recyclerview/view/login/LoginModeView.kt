package com.example.recyclerview.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recyclerview.data.UsersRepository
import com.example.recyclerview.data.prefer.UserModel
import kotlinx.coroutines.launch

class LoginModeView(private val repository: UsersRepository) : ViewModel() {
    fun saveSession(userModel: UserModel) {
        viewModelScope.launch {
            repository.saveSession(userModel)
        }
    }
}