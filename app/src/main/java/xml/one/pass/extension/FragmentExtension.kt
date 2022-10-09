package xml.one.pass.extension

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

inline fun Fragment.handleActivityBackPressed(
    crossinline backPressed: () -> Unit
) {
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                backPressed()
            }
        }
    )
}

/**
 * How to use in a Fragment
 * override fun onAttach(context: Context) {
 *      super.onAttach(context)
 *      handleActivityBackPressed {
 *          Toast.makeText(requireContext(), "Back pressed", Toast.LENGTH_LONG).show()
 *      }
 * }
 */
