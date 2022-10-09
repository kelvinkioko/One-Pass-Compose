package xml.one.pass.presentation.register

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xml.one.pass.R
import xml.one.pass.databinding.RegisterFragmentBinding
import xml.one.pass.extension.viewBinding
import xml.one.pass.util.ConstantsUtil
import xml.one.pass.util.ConstantsUtil.EMAIL_REGEX
import xml.one.pass.util.ConstantsUtil.NAME_LENGTH_MINIMUM_LIMIT

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.register_fragment) {

    private val binding by viewBinding(RegisterFragmentBinding::bind)
    private val viewModel: RegisterViewModel by viewModels()

    private var name = ""
    private var emailAddress = ""
    private var password = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOnClickListener()
        setUpObserver()
    }

    private fun setUpOnClickListener() {
        binding.apply {
            registerAction.setOnClickListener {
                viewModel.registerAccount(
                    name = name,
                    email = emailAddress,
                    password = password
                )
            }

            signInAction.setOnClickListener {
                findNavController().navigate(RegisterFragmentDirections.toLoginFragment())
            }
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerUiState.collectLatest { state ->
                    when (state) {
                        is RegisterUiState.Error ->
                            Snackbar.make(
                                binding.root,
                                state.message.asString(context = requireContext()),
                                Snackbar.LENGTH_LONG
                            ).show()
                        is RegisterUiState.Loading ->
                            Snackbar.make(
                                binding.root,
                                if (state.isLoading) "Loading" else "Not Loading",
                                Snackbar.LENGTH_LONG
                            ).show()
                        RegisterUiState.Success ->
                            findNavController().navigate(RegisterFragmentDirections.toHomeFragment())
                    }
                }
            }
        }
    }

    private fun setUpInputListener() {
        binding.apply {
            nameInput.editText?.doAfterTextChanged {
                name = if (it.isNullOrEmpty()) "" else it.toString()
                validateInputs()
            }
            emailAddressInput.editText?.doAfterTextChanged {
                emailAddress = if (it.isNullOrEmpty()) "" else it.toString()
                validateInputs()
            }
            passwordInput.editText?.doAfterTextChanged {
                password = if (it.isNullOrEmpty()) "" else it.toString()

                passwordInput.error = if (password.length < ConstantsUtil.PASSWORD_LENGTH_LIMIT)
                    "Password limit is 8 characters" else null
                validateInputs()
            }
        }
    }

    private fun validateInputs() {
        binding.apply {
            val nameValidation = name.length > NAME_LENGTH_MINIMUM_LIMIT && name.contains(" ")
            val emailValidation = EMAIL_REGEX.toRegex().matches(emailAddress)
            val passwordValidation = password.length >= ConstantsUtil.PASSWORD_LENGTH_LIMIT

            registerAction.isEnabled = nameValidation && emailValidation && passwordValidation
        }
    }

    private fun removeInputListener() {
        binding.apply {
            nameInput.editText?.doAfterTextChanged {}
            emailAddressInput.editText?.doAfterTextChanged {}
            passwordInput.editText?.doAfterTextChanged {}
        }
    }

    override fun onResume() {
        super.onResume()
        setUpInputListener()
    }

    override fun onPause() {
        super.onPause()
        removeInputListener()
    }
}
