package xml.one.pass.presentation.password.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xml.one.pass.MainDispatcherRule
import xml.one.pass.data.repository.PasswordRepositoryTestImpl
import xml.one.pass.util.TextResource

@OptIn(ExperimentalCoroutinesApi::class)
class PasswordDetailsViewModelTest {

    private lateinit var passwordDetailsViewModel: PasswordDetailsViewModel
    private val passwordRepository = PasswordRepositoryTestImpl()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        passwordDetailsViewModel = PasswordDetailsViewModel(
            passwordRepository = passwordRepository
        )
    }

    @Test
    fun `load password details by existing ID`() = runTest {
        passwordRepository.createTestPassword()
        passwordDetailsViewModel.loadPasswordDetails(passwordId = 1)

        passwordDetailsViewModel.uiState.test {
            Truth.assertThat(PasswordDetailsUiState.DefaultState).isEqualTo(awaitItem())

            val passwordDetails = passwordRepository.passwords.find { it.id == 1 }
            passwordDetails?.let { password ->
                Truth.assertThat(
                    PasswordDetailsUiState.PasswordDetails(password = password)
                ).isEqualTo(awaitItem())
            } ?: kotlin.run {
                Truth.assertThat(
                    PasswordDetailsUiState.Error(
                        message =
                        TextResource.DynamicString("Couldn't find the password details")
                    )
                ).isEqualTo(awaitItem())
            }
            cancel()
        }
    }

    @Test
    fun `load password details by a non existing ID`() = runTest {
        passwordRepository.createTestPassword()
        passwordDetailsViewModel.loadPasswordDetails(passwordId = 0)

        passwordDetailsViewModel.uiState.test {
            Truth.assertThat(PasswordDetailsUiState.DefaultState).isEqualTo(awaitItem())

            val passwordDetails = passwordRepository.passwords.find { it.id == 0 }
            passwordDetails?.let { password ->
                Truth.assertThat(
                    PasswordDetailsUiState.PasswordDetails(password = password)
                ).isEqualTo(awaitItem())
            } ?: kotlin.run {
                Truth.assertThat(
                    PasswordDetailsUiState.Error(
                        message =
                        TextResource.DynamicString("Couldn't find the password details")
                    )
                ).isEqualTo(awaitItem())
            }
            cancel()
        }
    }

    @Test
    fun `delete password by existing ID`() = runTest {
        passwordRepository.createTestPassword()
//        passwordDetailsViewModel.setPasswordID(passwordId = 0)
//        passwordDetailsViewModel.deletePasswordDetails()
//
//        Truth.assertThat(passwordDetailsViewModel.action).isEqualTo(GlobalAction.NavigateUp)
    }

    @Test
    fun `delete password by non existing ID`() = runTest {
        passwordRepository.createTestPassword()
//        passwordDetailsViewModel.setPasswordID(passwordId = 0)
//        passwordDetailsViewModel.deletePasswordDetails()
//
//        passwordDetailsViewModel.action.test {
//            Truth.assertThat(TextResource.StringResource(R.string.unable_delete)).isEqualTo(awaitItem())
//            cancel()
//        }
    }
}
