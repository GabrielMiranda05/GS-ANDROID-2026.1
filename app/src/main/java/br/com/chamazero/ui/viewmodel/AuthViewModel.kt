package br.com.windfyr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.windfyr.data.model.AuthResponse
import br.com.windfyr.data.repository.AuthRepository
import br.com.windfyr.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loginState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val loginState: StateFlow<Resource<AuthResponse>?> = _loginState

    private val _registerState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val registerState: StateFlow<Resource<AuthResponse>?> = _registerState

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onNameChange(value: String) { _name.value = value }
    fun onConfirmPasswordChange(value: String) { _confirmPassword.value = value }

    fun login() {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = repository.login(_email.value, _password.value)
        }
    }

    fun register() {
        if (_password.value != _confirmPassword.value) {
            _registerState.value = Resource.Error("As senhas não coincidem")
            return
        }
        viewModelScope.launch {
            _registerState.value = Resource.Loading
            _registerState.value = repository.register(_name.value, _email.value, _password.value)
        }
    }

    fun resetLoginState() { _loginState.value = null }
    fun resetRegisterState() { _registerState.value = null }
}
