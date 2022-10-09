package xml.one.pass.presentation.password.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.R
import xml.one.pass.common.GlobalAction
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import xml.one.pass.util.Resource
import xml.one.pass.util.TextResource
import javax.inject.Inject

@HiltViewModel
class PasswordDetailsViewModel @Inject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PasswordDetailsUiState>(PasswordDetailsUiState.DefaultState)
    var uiState = _uiState.asStateFlow()

    private val _action = Channel<GlobalAction>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    private var passwordID: Int = 0

    fun setPasswordID(passwordId: Int) {
        this.passwordID = passwordId
    }

    fun loadPasswordDetails(passwordId: Int) {
        this.passwordID = passwordId
        viewModelScope.launch {
            val password = withContext(Dispatchers.IO) {
                passwordRepository.loadPasswordById(passwordId = passwordId)
            }

            password.onEach { resource ->
                when (resource) {
                    is Resource.Error ->
                        _uiState.value = PasswordDetailsUiState.Error(
                            message = TextResource.DynamicString(resource.message ?: "")
                        )
                    is Resource.Success -> {
                        resource.data?.let { passwordModel ->
                            _uiState.value = PasswordDetailsUiState.PasswordDetails(
                                password = passwordModel
                            )
                        } ?: kotlin.run {
                            _uiState.value = PasswordDetailsUiState.Error(
                                message = TextResource.DynamicString(resource.message ?: "")
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun navigateToEditPasswordDetails() {
        viewModelScope.launch {
            val direction = PasswordDetailsFragmentDirections.toEditPasswordFragment(passwordID)
            _action.send(GlobalAction.Navigate(directions = direction))
        }
    }

    fun deletePasswordDetails() {
        viewModelScope.launch {
            val passwordDeleted = withContext(Dispatchers.IO) {
                passwordRepository.deletePasswordByID(id = passwordID)
            }

            if (passwordDeleted) {
                _action.send(GlobalAction.NavigateUp)
            } else {
                _uiState.value = PasswordDetailsUiState.Error(
                    message = TextResource.StringResource(R.string.unable_delete)
                )
            }
        }
    }
}

sealed class PasswordDetailsUiState {
    object DefaultState : PasswordDetailsUiState()

    data class PasswordDetails(
        val password: PasswordModel
    ) : PasswordDetailsUiState()

    data class Error(
        val message: TextResource
    ) : PasswordDetailsUiState()
}
