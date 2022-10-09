package xml.one.pass.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xml.one.pass.MainDispatcherRule
import xml.one.pass.data.repository.AccountRepositoryTestImpl
import xml.one.pass.domain.model.AccountModel

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private lateinit var profileViewModel: ProfileViewModel
    private val accountRepository = AccountRepositoryTestImpl()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        profileViewModel = ProfileViewModel(
            accountRepository = accountRepository
        )
    }

    @Test
    fun `get profile details`() = runTest {
        accountRepository.createTestAccount()
        profileViewModel.setUpProfilePage()

        profileViewModel.profileUiState.test {
            Truth.assertThat(ProfileUiState.StartState).isEqualTo(awaitItem())
            Truth.assertThat(
                ProfileUiState.ProfileDetails(
                    account = AccountModel(
                        id = 1,
                        name = "Kelvin Kioko",
                        email = "kiokokelvin@gmail.com",
                        password = "12345678"
                    )
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }
}
