package xml.one.pass.presentation.home

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

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel
    private val passwordRepository = PasswordRepositoryTestImpl()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        homeViewModel = HomeViewModel(
            passwordRepository = passwordRepository
        )
    }

    @Test
    fun `get password, passwords stored and compromised`() = runTest {
        passwordRepository.createTestPassword()
        homeViewModel.setUpHomePage()

        homeViewModel.uiState.test {
            Truth.assertThat(HomeUiState.HomeState).isEqualTo(awaitItem())
            Truth.assertThat(
                HomeUiState.Passwords(
                    passwords = passwordRepository.passwords
                )
            ).isEqualTo(awaitItem())
            cancel()
        }
    }
}
