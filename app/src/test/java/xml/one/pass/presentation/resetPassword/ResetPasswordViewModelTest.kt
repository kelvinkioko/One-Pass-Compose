package xml.one.pass.presentation.resetPassword

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

@OptIn(ExperimentalCoroutinesApi::class)
class ResetPasswordViewModelTest {

    private lateinit var resetPasswordViewModel: ResetPasswordViewModel
    private val accountRepository = AccountRepositoryTestImpl()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        resetPasswordViewModel = ResetPasswordViewModel(
            accountRepository = accountRepository
        )
    }

    @Test
    fun `attempt to reset password to an existing account`() = runTest {
        accountRepository.createTestAccount()
        resetPasswordViewModel.resetPassword(password = "87654321")

        resetPasswordViewModel.resetUiState.test {
            Truth.assertThat(ResetUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            Truth.assertThat(ResetUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(ResetUiState.Success).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `attempt to reset password to a non existing account`() = runTest {
        resetPasswordViewModel.resetPassword(password = "87654321")

        resetPasswordViewModel.resetUiState.test {
            Truth.assertThat(ResetUiState.Loading(isLoading = false)).isEqualTo(awaitItem())
            Truth.assertThat(ResetUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(
                ResetUiState.Error(
                    message = TextResource.StringResource(R.string.reset_password_error)
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }
}
