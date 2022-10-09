package xml.one.pass.presentation.password.manage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xml.one.pass.MainDispatcherRule
import xml.one.pass.data.local.mapper.dateFormatter
import xml.one.pass.data.repository.PasswordRepositoryTestImpl
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.extension.getCurrentDate
import xml.one.pass.util.TextResource

@OptIn(ExperimentalCoroutinesApi::class)
class BasePasswordViewModelTest {

    private lateinit var basePasswordViewModel: BasePasswordViewModel
    private val passwordRepository = PasswordRepositoryTestImpl()

    private val passwordModel = PasswordModel(
        id = 1,
        siteName = "Sample Site",
        url = "https://www.sample.com",
        userName = "sample",
        email = "sample@mail.com",
        password = "12345678",
        phoneNumber = "(+254) 720 000 000",
        securityQuestions = "",
        timeCreated = getCurrentDate().dateFormatter(),
        timeUpdated = getCurrentDate().dateFormatter()
    )

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        basePasswordViewModel = BasePasswordViewModel(
            passwordRepository = passwordRepository
        )
    }

    @Test
    fun `load password details by existing ID`() = runTest {
        passwordRepository.createTestPassword()
        basePasswordViewModel.setPassword(passwordId = 1)

        basePasswordViewModel.uiState.test {
            Truth.assertThat(BasePasswordUiState.Loading()).isEqualTo(awaitItem())

            val passwordDetails = passwordRepository.passwords.find { it.id == 1 }
            passwordDetails?.let { password ->
                Truth.assertThat(
                    BasePasswordUiState.PasswordDetails(password = password)
                ).isEqualTo(awaitItem())
            } ?: kotlin.run {
                Truth.assertThat(
                    BasePasswordUiState.Error(
                        errorMessage =
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
        basePasswordViewModel.setPassword(passwordId = 2)

        basePasswordViewModel.uiState.test {
            Truth.assertThat(BasePasswordUiState.Loading()).isEqualTo(awaitItem())

            val passwordDetails = passwordRepository.passwords.find { it.id == 2 }
            passwordDetails?.let { password ->
                Truth.assertThat(
                    BasePasswordUiState.PasswordDetails(password = password)
                ).isEqualTo(awaitItem())
            } ?: kotlin.run {
                Truth.assertThat(
                    BasePasswordUiState.Error(
                        errorMessage =
                        TextResource.DynamicString("Couldn't find the password details")
                    )
                ).isEqualTo(awaitItem())
            }
            cancel()
        }
    }

    @Test
    fun `save a new password`() = runTest {
        basePasswordViewModel.savePassword(passwordModel = passwordModel)

        basePasswordViewModel.uiState.test {
            Truth.assertThat(BasePasswordUiState.Loading()).isEqualTo(awaitItem())
            Truth.assertThat(BasePasswordUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(BasePasswordUiState.Loading()).isEqualTo(awaitItem())
            Truth.assertThat(BasePasswordUiState.Success).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `update an existing password`() = runTest {
        passwordRepository.createTestPassword(password = passwordModel)
        basePasswordViewModel.savePassword(passwordModel = passwordModel.also { it.id = 1 })

        basePasswordViewModel.uiState.test {
            Truth.assertThat(awaitItem()).isEqualTo(BasePasswordUiState.Loading())
            Truth.assertThat(awaitItem()).isEqualTo(BasePasswordUiState.Loading(isLoading = true))
            Truth.assertThat(awaitItem()).isEqualTo(BasePasswordUiState.Loading())
            Truth.assertThat(awaitItem()).isEqualTo(BasePasswordUiState.Success)
            cancel()
        }
    }

    @Test
    fun `update an existing password with a non existing ID`() = runTest {
        passwordRepository.createTestPassword()
        basePasswordViewModel.setPassword(passwordId = -1)
        basePasswordViewModel.savePassword(passwordModel = passwordModel)

        basePasswordViewModel.uiState.test {
            Truth.assertThat(BasePasswordUiState.Loading()).isEqualTo(awaitItem())
            Truth.assertThat(BasePasswordUiState.Loading(isLoading = true)).isEqualTo(awaitItem())
            Truth.assertThat(BasePasswordUiState.Loading()).isEqualTo(awaitItem())
            Truth.assertThat(awaitItem()).isEqualTo(
                BasePasswordUiState.Error(
                    errorMessage = TextResource.DynamicString("Password doesn't exist!")
                )
            )
            cancel()
        }
    }
}
