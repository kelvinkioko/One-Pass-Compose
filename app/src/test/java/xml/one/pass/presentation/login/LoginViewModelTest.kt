package xml.one.pass.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xml.one.pass.MainDispatcherRule
import xml.one.pass.R
import xml.one.pass.data.preference.OnePassRepositoryTestImpl
import xml.one.pass.data.repository.AccountRepositoryTestImpl
import xml.one.pass.util.TextResource

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    private val accountRepository = AccountRepositoryTestImpl()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(
            accountRepository = accountRepository,
            onePassRepository = OnePassRepositoryTestImpl()
        )
    }

    @Test
    fun `attempt to login with no registered accounts`() = runTest {
        loginViewModel.login(email = "kiokokelvin@gmail.com", password = "12345678")

        loginViewModel.loginUiState.test {
            assertThat(LoginUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            assertThat(LoginUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            assertThat(
                LoginUiState.Error(
                    message = TextResource.StringResource(R.string.login_credentials_error)
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to login with empty email and password`() = runTest {
        loginViewModel.login(email = "", password = "")

        loginViewModel.loginUiState.test {
            assertThat(LoginUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            assertThat(LoginUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            assertThat(
                LoginUiState.Error(
                    message = TextResource.StringResource(R.string.login_credentials_error)
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to login with valid email and password`() = runTest {
        accountRepository.createTestAccount()
        loginViewModel.login(email = "kiokokelvin@gmail.com", password = "12345678")

        loginViewModel.loginUiState.test {
            assertThat(LoginUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            assertThat(LoginUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            assertThat(LoginUiState.Success).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to login with invalid email and password`() = runTest {
        loginViewModel.login(email = "kelvinkioko@gmail.com", password = "87654321")

        loginViewModel.loginUiState.test {
            assertThat(LoginUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            assertThat(LoginUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            assertThat(
                LoginUiState.Error(
                    message = TextResource.StringResource(R.string.login_credentials_error)
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to login with invalid email`() = runTest {
        loginViewModel.login(email = "kelvinkioko@gmail.com", password = "12345678")

        loginViewModel.loginUiState.test {
            assertThat(LoginUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            assertThat(LoginUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            assertThat(
                LoginUiState.Error(
                    message = TextResource.StringResource(R.string.login_credentials_error)
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to login with invalid password`() = runTest {
        loginViewModel.login(email = "kiokokelvin@gmail.com", password = "87654321")

        loginViewModel.loginUiState.test {
            assertThat(LoginUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            assertThat(LoginUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            assertThat(
                LoginUiState.Error(
                    message = TextResource.StringResource(R.string.login_credentials_error)
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }
}
