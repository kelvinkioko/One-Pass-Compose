package xml.one.pass.presentation.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.R
import xml.one.pass.domain.repository.AccountRepository
import xml.one.pass.util.TextResource
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _resetUiState = MutableStateFlow<ResetUiState>(
        ResetUiState.Loading()
    )
    val resetUiState = _resetUiState.asStateFlow()

    fun resetPassword(password: String) {
        viewModelScope.launch {
            _resetUiState.value = ResetUiState.Loading(isLoading = true)
            withContext(Dispatchers.IO) {
                val account = accountRepository.loadAccount()
                val updatedPassword = accountRepository.updateAccountPassword(
                    password = password,
                    id = account?.id ?: 0
                )

                withContext(Dispatchers.Main) {
                    _resetUiState.value = ResetUiState.Loading(isLoading = false)
                    _resetUiState.value = if (updatedPassword == 1) {
                        ResetUiState.Success
                    } else {
                        ResetUiState.Error(
                            message = TextResource.StringResource(R.string.reset_password_error)
                        )
                    }
                }
            }
        }
    }
}

sealed class ResetUiState {
    data class Loading(val isLoading: Boolean = false) : ResetUiState()

    data class Error(val message: TextResource) : ResetUiState()

    object Success : ResetUiState()
}
