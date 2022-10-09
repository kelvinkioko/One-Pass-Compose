package xml.one.pass.presentation.password.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.R
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import xml.one.pass.util.Resource
import xml.one.pass.util.TextResource
import javax.inject.Inject

@HiltViewModel
class BasePasswordViewModel @Inject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BasePasswordUiState>(BasePasswordUiState.Loading())
    val uiState = _uiState.asStateFlow()

    private var passwordId: Int = 0

    fun setPassword(passwordId: Int) {
        this.passwordId = passwordId

        if (passwordId > 0)
            preparePage()
    }

    private fun preparePage() {
        viewModelScope.launch {
            val password = withContext(Dispatchers.IO) {
                passwordRepository.loadPasswordById(passwordId = passwordId)
            }

            password.onEach { resource ->
                when (resource) {
                    is Resource.Error ->
                        _uiState.value = BasePasswordUiState.Error(
                            errorMessage = TextResource.DynamicString(resource.message ?: "")
                        )
                    is Resource.Success -> {
                        resource.data?.let { passwordModel ->
                            _uiState.value = BasePasswordUiState.PasswordDetails(
                                password = passwordModel
                            )
                        } ?: kotlin.run {
                            _uiState.value = BasePasswordUiState.Error(
                                errorMessage = TextResource.DynamicString(resource.message ?: "")
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun savePassword(
        passwordModel: PasswordModel
    ) {
        if (passwordId == 0) {
            createPassword(passwordModel = passwordModel)
        } else {
            updatePassword(passwordModel = passwordModel.also { it.id = passwordId })
        }
    }

    private fun updatePassword(passwordModel: PasswordModel) {
        viewModelScope.launch {
            _uiState.value = BasePasswordUiState.Loading(isLoading = true)
            val status = withContext(Dispatchers.IO) {
                passwordRepository.updatePasswordDetails(passwordModel = passwordModel)
            }

            _uiState.value = BasePasswordUiState.Loading(isLoading = false)

            status.onEach { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _uiState.value = BasePasswordUiState.Error(
                            errorMessage = TextResource.DynamicString(resource.message ?: "")
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = if (resource.data == true)
                            BasePasswordUiState.Success
                        else
                            BasePasswordUiState.Error(
                                errorMessage = TextResource.StringResource(R.string.save_password_error)
                            )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun createPassword(passwordModel: PasswordModel) {
        viewModelScope.launch {
            _uiState.value = BasePasswordUiState.Loading(isLoading = true)
            val status = withContext(Dispatchers.IO) {
                passwordRepository.insertPassword(passwordModel = passwordModel)
            }

            _uiState.value = BasePasswordUiState.Loading(isLoading = false)

            status.onEach { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _uiState.value = BasePasswordUiState.Error(
                            errorMessage = TextResource.DynamicString(resource.message ?: "")
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = if (resource.data == true)
                            BasePasswordUiState.Success
                        else
                            BasePasswordUiState.Error(
                                errorMessage = TextResource.StringResource(R.string.save_password_error)
                            )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}

sealed class BasePasswordUiState {
    object Success : BasePasswordUiState()

    data class Loading(val isLoading: Boolean = false) : BasePasswordUiState()

    data class PasswordDetails(val password: PasswordModel) : BasePasswordUiState()

    data class Error(val errorMessage: TextResource) : BasePasswordUiState()
}
