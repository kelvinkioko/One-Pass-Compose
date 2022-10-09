package xml.one.pass.presentation.register

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
class RegisterViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Loading())
    val registerUiState = _registerUiState.asStateFlow()

    fun registerAccount(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerUiState.value = RegisterUiState.Loading(isLoading = true)
            withContext(Dispatchers.IO) {
                delay(DELAY_TIME_2000)
                if (accountRepository.doesAccountExistWithEmail(email = email)) {
                    withContext(Dispatchers.Main) {
                        _registerUiState.value = RegisterUiState.Loading(isLoading = false)
                        _registerUiState.value = RegisterUiState.Error(
                            message = TextResource.StringResource(R.string.account_exists_error)
                        )
                    }
                } else if (accountRepository.areThereAccounts()) {
                    val existingAccount = accountRepository.loadAccount()
                    withContext(Dispatchers.Main) {
                        _registerUiState.value = RegisterUiState.Loading(isLoading = false)
                        _registerUiState.value = RegisterUiState.Error(
                            message = TextResource.DynamicString(
                                "An account already exists " +
                                    "with the email ${existingAccount?.email}. Proceed to login."
                            )
                        )
                    }
                } else {
                    accountRepository.insertAccount(
                        name = name,
                        email = email,
                        password = password
                    )
                    withContext(Dispatchers.Main) {
                        _registerUiState.value = RegisterUiState.Loading(isLoading = false)
                        _registerUiState.value = RegisterUiState.Success
                    }
                }
            }
        }
    }
}

sealed class RegisterUiState {
    data class Loading(val isLoading: Boolean = false) : RegisterUiState()

    data class Error(val message: TextResource) : RegisterUiState()

    object Success : RegisterUiState()
}
