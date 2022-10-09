package xml.one.pass.presentation.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xml.one.pass.MainDispatcherRule
import xml.one.pass.R
import xml.one.pass.data.repository.AccountRepositoryTestImpl
import xml.one.pass.util.TextResource

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    private lateinit var registerViewModel: RegisterViewModel
    private val accountRepository = AccountRepositoryTestImpl()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(
            accountRepository = accountRepository
        )
    }

    @Test
    fun `attempt to register a valid existing account`() = runTest {
        accountRepository.createTestAccount()
        registerViewModel.registerAccount(
            name = "Kelvin Kioko",
            email = "kiokokelvin@gmail.com",
            password = "12345678"
        )

        registerViewModel.registerUiState.test {
            Truth.assertThat(RegisterUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            Truth.assertThat(RegisterUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(
                RegisterUiState.Error(
                    message = TextResource.StringResource(R.string.account_exists_error)
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to register a second local account`() = runTest {
        accountRepository.createTestAccount()
        registerViewModel.registerAccount(
            name = "Takezo Kensei",
            email = "takezokensei@gmail.com",
            password = "12345678"
        )

        registerViewModel.registerUiState.test {
            Truth.assertThat(RegisterUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            Truth.assertThat(RegisterUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(
                RegisterUiState.Error(
                    message = TextResource.DynamicString(
                        "An account already exists with " +
                            "the email ${accountRepository.loadAccount()?.email}. Proceed to login."
                    )
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to register a valid account`() = runTest {
        accountRepository.deleteAccount()
        registerViewModel.registerAccount(
            name = "Takezo Kensei",
            email = "takezokensei@gmail.com",
            password = "12345678"
        )

        registerViewModel.registerUiState.test {
            Truth.assertThat(RegisterUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            Truth.assertThat(RegisterUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(RegisterUiState.Success).isEqualTo(awaitItem())
            cancel()
        }
    }
}
