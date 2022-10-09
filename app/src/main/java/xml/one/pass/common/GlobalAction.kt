package xml.one.pass.common

import androidx.navigation.NavDirections

sealed class GlobalAction {
    object NavigateUp : GlobalAction()

    data class Navigate(val directions: NavDirections) : GlobalAction()
}
