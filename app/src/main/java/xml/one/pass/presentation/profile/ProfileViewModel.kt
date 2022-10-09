package xml.one.pass.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.domain.model.AccountModel
import xml.one.pass.domain.repository.AccountRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.StartState)
    val profileUiState = _profileUiState.asStateFlow()

    init {
        setUpProfilePage()
    }

    fun setUpProfilePage() {
        viewModelScope.launch {
            val account = withContext(Dispatchers.IO) { accountRepository.loadAccount() }

            _profileUiState.value = ProfileUiState.ProfileDetails(account = account)
        }
    }
}

sealed class ProfileUiState {
    object StartState : ProfileUiState()

    data class ProfileDetails(val account: AccountModel?) : ProfileUiState()
}
