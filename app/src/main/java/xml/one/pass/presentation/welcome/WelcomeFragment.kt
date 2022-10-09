package xml.one.pass.presentation.welcome

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import xml.one.pass.R
import xml.one.pass.databinding.WelcomeFragmentBinding
import xml.one.pass.extension.viewBinding
import xml.one.pass.util.ConstantsUtil.OXFFFFFF
import java.util.Locale

class WelcomeFragment : Fragment(R.layout.welcome_fragment) {

    private val binding by viewBinding(WelcomeFragmentBinding::bind)

    private var currentStep = 0
    private var stepCount = arrayListOf("1", "2", "3")
    private var wordCount = intArrayOf(1, 2, 2)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOnClickListener()
        setUpStep()
        setUpWelcomeTitleManager()
    }

    private fun setUpOnClickListener() {
        binding.apply {
            welcomeTitle.setOnClickListener {
                if (currentStep < 2) {
                    currentStep++
                    setUpStep()
                    setUpWelcomeTitleManager()
                }
            }

            registerAction.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.toRegisterFragment())
            }

            loginAction.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.toLoginFragment())
            }
        }
    }

    private fun setUpStep() {
        binding.apply {
            var steps = ""

            for (i in stepCount.indices) {
                steps += when (i) {
                    currentStep ->
                        getBoldColoredStepSpanned(text = stepCount[i], color = getHexColor()) + " "
                    stepCount.size - 1 -> stepCount[i]
                    else -> stepCount[i] + " "
                }
            }
            welcomePageCounter.text = HtmlCompat.fromHtml(steps, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun setUpWelcomeTitleManager() {
        val wordToChange = wordCount[currentStep]
        val title = getString(
            when (currentStep) {
                0 -> R.string.welcome_one_title
                1 -> R.string.welcome_two_title
                else -> R.string.welcome_three_title
            }
        )
        val message = getString(
            when (currentStep) {
                0 -> R.string.welcome_one_description
                1 -> R.string.welcome_two_description
                else -> R.string.welcome_three_description
            }
        )
        val titleArray = title.split(" ")

        var finalTitle = ""

        for (i in titleArray.indices) {
            finalTitle += when (i) {
                wordToChange ->
                    getColoredTitleSpanned(text = titleArray[i], color = getHexColor()) + " "
                titleArray.size - 1 -> titleArray[i]
                else -> titleArray[i] + " "
            }
        }
        binding.apply {
            welcomeTitle.text = HtmlCompat.fromHtml(finalTitle, HtmlCompat.FROM_HTML_MODE_LEGACY)
            welcomeMessage.text = message
        }
    }

    private fun getBoldColoredStepSpanned(text: String, color: String): String =
        "<font color=$color><b>$text</b></font>"

    private fun getColoredTitleSpanned(text: String, color: String): String =
        "<font color=$color>$text</font>"

    private fun getHexColor(): String = String.format(
        Locale.getDefault(),
        "#%06X",
        OXFFFFFF and ContextCompat.getColor(requireContext(), R.color.brand_color)
    )
}
