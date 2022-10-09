package xml.one.pass.presentation.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.R
import xml.one.pass.domain.repository.AccountRepository
import xml.one.pass.util.ConstantsUtil.DELAY_TIME_2000
import xml.one.pass.util.TextResource
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _forgotPasswordUiState = MutableStateFlow<ForgotPasswordUiState>(
        ForgotPasswordUiState.Loading()
    )
    val forgotPasswordUiState = _forgotPasswordUiState.asStateFlow()

    fun checkAccountExists(email: String) {
        viewModelScope.launch {
            _forgotPasswordUiState.value = ForgotPasswordUiState.Loading(isLoading = true)
            withContext(Dispatchers.IO) {
                delay(DELAY_TIME_2000)
                val accountExists = accountRepository.doesAccountExistWithEmail(email = email)
                withContext(Dispatchers.Main) {
                    _forgotPasswordUiState.value = ForgotPasswordUiState.Loading(isLoading = false)
                    _forgotPasswordUiState.value = if (accountExists)
                        ForgotPasswordUiState.Success
                    else
                        ForgotPasswordUiState.Error(
                            message = TextResource.StringResource(R.string.no_account_by_email)
                        )
                }
            }
        }
    }
}

sealed class ForgotPasswordUiState {
    data class Loading(val isLoading: Boolean = false) : ForgotPasswordUiState()

    data class Error(val message: TextResource) : ForgotPasswordUiState()

    object Success : ForgotPasswordUiState()
}
