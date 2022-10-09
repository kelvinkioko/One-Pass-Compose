package xml.one.pass.presentation.forgotPassword

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
class ForgotPasswordViewModelTest {

    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    private val accountRepository = AccountRepositoryTestImpl()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        accountRepository.createTestAccount()
        forgotPasswordViewModel = ForgotPasswordViewModel(
            accountRepository = accountRepository
        )
    }

    @Test
    fun `attempt to check account exists with empty email`() = runTest {
        forgotPasswordViewModel.checkAccountExists(email = "")

        forgotPasswordViewModel.forgotPasswordUiState.test {
            Truth.assertThat(ForgotPasswordUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            Truth.assertThat(ForgotPasswordUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(
                ForgotPasswordUiState.Error(
                    message = TextResource.StringResource(R.string.no_account_by_email)
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to check account exists with invalid email`() = runTest {
        forgotPasswordViewModel.checkAccountExists(email = "kelvinkioko@gmail.com")

        forgotPasswordViewModel.forgotPasswordUiState.test {
            Truth.assertThat(ForgotPasswordUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            Truth.assertThat(ForgotPasswordUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(
                ForgotPasswordUiState.Error(
                    message = TextResource.StringResource(R.string.no_account_by_email)
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to check account exists with valid email`() = runTest {
        forgotPasswordViewModel.checkAccountExists(email = "kiokokelvin@gmail.com")

        forgotPasswordViewModel.forgotPasswordUiState.test {
            Truth.assertThat(ForgotPasswordUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            Truth.assertThat(ForgotPasswordUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(ForgotPasswordUiState.Success).isEqualTo(awaitItem())
            cancel()
        }
    }
}
