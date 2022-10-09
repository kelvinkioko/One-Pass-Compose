package xml.one.pass.presentation.password.manage

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import xml.one.pass.R
import xml.one.pass.data.local.mapper.dateFormatter
import xml.one.pass.databinding.PasswordAddFragmentBinding
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.extension.getCurrentDate
import xml.one.pass.extension.observeState
import xml.one.pass.extension.viewBinding

@AndroidEntryPoint
open class BasePasswordManagerFragment(
    @StringRes private val buttonTitle: Int,
    @StringRes private val pageTitle: Int
) : Fragment(R.layout.password_add_fragment) {

    open val binding by viewBinding(PasswordAddFragmentBinding::bind)
    open val viewModel: BasePasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        setToolbar()
        setUpClickListener()
        setUpObservers()
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setData() {
        binding.apply {
            saveAction.text = getString(buttonTitle)
            welcomeTitle.text = getString(pageTitle)
        }
    }

    private fun setUpClickListener() {
        binding.apply {
            saveAction.setOnClickListener {
                if (nameInput.editText?.text.toString().isEmpty())
                    nameInput.error = "Name is required!"
                else if (passwordInput.editText?.text.toString().isEmpty())
                    passwordInput.error = "Password is required!"
                else if (
                    userNameInput.editText?.text.toString().isEmpty() &&
                    emailAddressInput.editText?.text.toString().isEmpty() &&
                    phoneNumberInput.editText?.text.toString().isEmpty()
                ) {
                    userNameInput.error = "Email/username/phone number is required!"
                    emailAddressInput.error = "Email/username/phone number is required!"
                    phoneNumberInput.error = "Email/username/phone number is required!"
                } else {
                    viewModel.savePassword(
                        passwordModel = PasswordModel(
                            siteName = nameInput.editText?.text.toString().trim(),
                            url = websiteInput.editText?.text.toString().trim(),
                            userName = userNameInput.editText?.text.toString().trim(),
                            email = emailAddressInput.editText?.text.toString().trim(),
                            password = passwordInput.editText?.text.toString().trim(),
                            phoneNumber = phoneNumberInput.editText?.text.toString().trim(),
                            timeCreated = getCurrentDate().dateFormatter(),
                            timeUpdated = getCurrentDate().dateFormatter()
                        )
                    )
                }
            }
        }
    }

    private fun setUpObservers() {
        viewModel.uiState.observeState(
            lifecycleOwner = this,
            lifecycleState = Lifecycle.State.STARTED
        ) { state ->
            when (state) {
                BasePasswordUiState.Success -> findNavController().navigateUp()
                is BasePasswordUiState.Loading -> Unit
                is BasePasswordUiState.Error ->
                    Snackbar.make(
                        binding.root,
                        state.errorMessage.asString(context = requireContext()),
                        Snackbar.LENGTH_LONG
                    ).show()
                is BasePasswordUiState.PasswordDetails ->
                    renderPasswordDetails(passwordDetails = state.password)
            }
        }
    }

    private fun renderPasswordDetails(passwordDetails: PasswordModel) {
        binding.apply {
            nameInput.editText?.setText(passwordDetails.siteName)
            websiteInput.editText?.setText(passwordDetails.url)
            userNameInput.editText?.setText(passwordDetails.userName)
            emailAddressInput.editText?.setText(passwordDetails.email)
            passwordInput.editText?.setText(passwordDetails.password)
            phoneNumberInput.editText?.setText(passwordDetails.phoneNumber)
        }
    }

    private fun setInputObservers() {
        binding.apply {
            nameInput.editText?.doAfterTextChanged { nameInput.error = null }
            websiteInput.editText?.doAfterTextChanged { websiteInput.error = null }
            userNameInput.editText?.doAfterTextChanged { invalidateUserIdentifierError() }
            emailAddressInput.editText?.doAfterTextChanged { invalidateUserIdentifierError() }
            passwordInput.editText?.doAfterTextChanged { passwordInput.error = null }
            phoneNumberInput.editText?.doAfterTextChanged { invalidateUserIdentifierError() }
        }
    }

    private fun invalidateUserIdentifierError() {
        binding.apply {
            userNameInput.error = null
            emailAddressInput.error = null
            phoneNumberInput.error = null
        }
    }

    private fun removeInputObservers() {
        binding.apply {
            nameInput.editText?.doAfterTextChanged {}
            websiteInput.editText?.doAfterTextChanged {}
            userNameInput.editText?.doAfterTextChanged {}
            emailAddressInput.editText?.doAfterTextChanged {}
            passwordInput.editText?.doAfterTextChanged {}
            phoneNumberInput.editText?.doAfterTextChanged {}
        }
    }

    override fun onResume() {
        super.onResume()
        setInputObservers()
    }

    override fun onPause() {
        super.onPause()
        removeInputObservers()
    }
}
