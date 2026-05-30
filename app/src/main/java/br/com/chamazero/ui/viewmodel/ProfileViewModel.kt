package br.com.windfyr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.windfyr.data.model.User
import br.com.windfyr.data.repository.UserRepository
import br.com.windfyr.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _profileState = MutableStateFlow<Resource<User>>(Resource.Loading)
    val profileState: StateFlow<Resource<User>> = _profileState

    private val _updateState = MutableStateFlow<Resource<User>?>(null)
    val updateState: StateFlow<Resource<User>?> = _updateState

    private val _editName = MutableStateFlow("")
    val editName: StateFlow<String> = _editName

    private val _editEmail = MutableStateFlow("")
    val editEmail: StateFlow<String> = _editEmail

    fun onEditNameChange(value: String) {
        if (value.length <= 60) _editName.value = value
    }

    fun onEditEmailChange(value: String) {
        if (value.length <= 80) _editEmail.value = value
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = Resource.Loading
            _profileState.value = repository.getProfile()
        }
    }

    fun prepareEdit(user: User) {
        _editName.value = user.name
        _editEmail.value = user.email
    }

    fun saveProfile() {
        viewModelScope.launch {
            _updateState.value = Resource.Loading
            _updateState.value = repository.updateProfile(_editName.value, _editEmail.value)
        }
    }

    fun resetUpdateState() { _updateState.value = null }
}
