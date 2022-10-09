package xml.one.pass.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.common.GlobalAction
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.HomeState)
    val uiState = _uiState.asStateFlow()

    private val _action = Channel<GlobalAction>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    init {
        setUpHomePage()
    }

    fun setUpHomePage() {
        viewModelScope.launch {
            val passwords = withContext(Dispatchers.IO) {
                passwordRepository.loadPassword()
            }

            _uiState.value = HomeUiState.PasswordStored(
                passwordStored = passwords.size.toString()
            )
            _uiState.value = HomeUiState.PasswordCompromised(
                passwordCompromised = "0"
            )
            _uiState.value = HomeUiState.Passwords(
                passwords = passwords
            )
        }
    }

    fun navigateToPasswordDetails(passwordID: Int) {
        viewModelScope.launch {
            val direction = HomeFragmentDirections.toPasswordDetailsFragment(passwordID)
            _action.send(GlobalAction.Navigate(directions = direction))
        }
    }
}

sealed class HomeUiState {
    object HomeState : HomeUiState()

    data class PasswordStored(val passwordStored: String) : HomeUiState()

    data class PasswordCompromised(val passwordCompromised: String) : HomeUiState()

    data class Passwords(val passwords: List<PasswordModel>) : HomeUiState()
}
