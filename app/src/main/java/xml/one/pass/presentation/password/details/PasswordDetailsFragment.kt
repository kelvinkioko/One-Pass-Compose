package xml.one.pass.presentation.password.details

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import xml.one.pass.R
import xml.one.pass.common.GlobalAction
import xml.one.pass.databinding.PasswordDetailsFragmentBinding
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.extension.copyPassword
import xml.one.pass.extension.displayDialog
import xml.one.pass.extension.maskString
import xml.one.pass.extension.observeEvent
import xml.one.pass.extension.observeState
import xml.one.pass.extension.viewBinding

@AndroidEntryPoint
class PasswordDetailsFragment : Fragment(R.layout.password_details_fragment) {

    private val binding by viewBinding(PasswordDetailsFragmentBinding::bind)
    private val viewModel: PasswordDetailsViewModel by viewModels()
    private val args: PasswordDetailsFragmentArgs by navArgs()

    private var dialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar()
        setClickListener()
        setObserver()

        viewModel.loadPasswordDetails(passwordId = args.passwordID)
    }

    private fun setToolBar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setClickListener() {
        binding.apply {
            updateAction.setOnClickListener {
                viewModel.navigateToEditPasswordDetails()
            }

            deleteAction.setOnClickListener {
                dialog?.dismiss()
                dialog = Dialog(requireContext())
                dialog?.displayDialog(
                    headline = "Delete password",
                    supportingText = "You are about to delete ${welcomeTitle.text} password details. Are you sure?",
                    isDecisionVisible = true,
                    decisionLabel = "Delete",
                    onDecisionCallBack = {
                        viewModel.deletePasswordDetails()
                    }
                )
            }
        }
    }

    private fun setObserver() {
        viewModel.uiState.observeState(this, Lifecycle.State.STARTED) { state ->
            when (state) {
                PasswordDetailsUiState.DefaultState -> Unit
                is PasswordDetailsUiState.Error -> {
                    dialog?.dismiss()
                    dialog = Dialog(requireContext())
                    dialog?.displayDialog(
                        headline = "Headline error",
                        supportingText = state.message.asString(context = requireContext())
                    )
                }
                is PasswordDetailsUiState.PasswordDetails ->
                    renderPasswordDetails(password = state.password)
            }
        }

        viewModel.action.observeEvent(
            lifecycleOwner = this,
            lifecycleState = Lifecycle.State.STARTED
        ) { action ->
            when (action) {
                is GlobalAction.Navigate -> findNavController().navigate(action.directions)
                GlobalAction.NavigateUp -> findNavController().navigateUp()
            }
        }
    }

    private fun renderPasswordDetails(password: PasswordModel) {
        binding.apply {
            welcomeTitle.text = password.siteName
            websiteValue.text = password.url
            userNameValue.text = password.userName
            emailAddressValue.text = password.email
            passwordValue.text = password.password.maskString()
            phoneNumberValue.text = password.phoneNumber
            updatedValue.text = password.timeUpdated.asDate()
            createdValue.text = password.timeCreated.asDate()

            passwordShowHideAction.setOnCheckedChangeListener { _, isChecked ->
                passwordValue.text = if (isChecked)
                    password.password
                else
                    password.password.maskString()
            }

            passwordCopyAction.setOnCheckedChangeListener { _, _ ->
                password.password.copyPassword(context = requireContext())
            }
        }
    }
}
