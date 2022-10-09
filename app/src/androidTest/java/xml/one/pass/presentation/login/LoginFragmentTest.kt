package xml.one.pass.presentation.login

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xml.one.pass.R
import xml.one.pass.launchFragmentInHiltContainer
import xml.one.pass.presentation.welcome.WelcomeFragmentDirections
import xml.one.pass.util.textInputMatcherWithHint

@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var navController: NavController

    @Before
    fun setup() {
        hiltRule.inject()
        navController = TestNavHostController(ApplicationProvider.getApplicationContext()).also {
            runOnUiThread {
                it.setGraph(R.navigation.app_nav_graph)
                it.navigate(WelcomeFragmentDirections.toLoginFragment())
            }
        }
    }

    @Test
    fun fragmentLaunchesSuccessfully() {
        launchFragmentInHiltContainer<LoginFragment>(themeResId = R.style.Theme_OnePass)
    }

    @Test
    fun fragmentCopyIsAccurate() {
        launchFragmentInHiltContainer<LoginFragment>(themeResId = R.style.Theme_OnePass)

        val resources = ApplicationProvider.getApplicationContext<Context>().resources

        onView(withId(R.id.appLogo)).check(
            matches(ViewMatchers.withContentDescription(R.string.one_pass_logo))
        )

        onView(withId(R.id.welcomeTitle)).check(
            matches(ViewMatchers.withText(R.string.login))
        )

        onView(withId(R.id.welcomeMessage)).check(
            matches(ViewMatchers.withText(R.string.login_guide))
        )

        onView(withId(R.id.emailAddressInput)).check(
            matches(textInputMatcherWithHint(resources.getString(R.string.email_address)))
        )

        onView(withId(R.id.passwordInput)).check(
            matches(textInputMatcherWithHint(resources.getString(R.string.password)))
        )

        onView(withId(R.id.signInForgot)).check(
            matches(ViewMatchers.withText(R.string.forgot_password_question))
        )

        onView(withId(R.id.signInAction)).check(
            matches(ViewMatchers.withText(R.string.sign_in))
        )

        onView(withId(R.id.signUpTitle)).check(
            matches(ViewMatchers.withText(R.string.don_t_have_an_account))
        )

        onView(withId(R.id.signUpAction)).check(
            matches(ViewMatchers.withText(R.string.sign_up))
        )
    }

    @Test
    fun clickForgotPassword() {
        launchFragmentInHiltContainer<LoginFragment>(themeResId = R.style.Theme_OnePass) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.signInForgot)).perform(click())

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.forgotPasswordFragment)
    }

    @Test
    fun clickSignUp() {
        launchFragmentInHiltContainer<LoginFragment>(themeResId = R.style.Theme_OnePass) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.signUpAction)).perform(click())

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.registerFragment)
    }
}
