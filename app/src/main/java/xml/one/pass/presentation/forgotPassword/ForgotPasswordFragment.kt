package xml.one.pass.presentation.forgotPassword

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
import xml.one.pass.databinding.ForgotPasswordFragmentBinding
import xml.one.pass.extension.viewBinding
import xml.one.pass.util.ConstantsUtil

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.forgot_password_fragment) {

    private val binding by viewBinding(ForgotPasswordFragmentBinding::bind)
    private val viewModel: ForgotPasswordViewModel by viewModels()

    private var emailAddress = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setUpOnClickListener()
        setUpObserver()
    }

    private fun setupToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setUpOnClickListener() {
        binding.apply {
            submitAction.setOnClickListener {
                if (ConstantsUtil.EMAIL_REGEX.toRegex().matches(emailAddress)) {
                    viewModel.checkAccountExists(email = emailAddress)
                } else {
                    emailAddressInput.error = "Please enter a valid email address"
                }
            }
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forgotPasswordUiState.collectLatest { state ->
                    when (state) {
                        is ForgotPasswordUiState.Error ->
                            Snackbar.make(
                                binding.root,
                                state.message.asString(context = requireContext()),
                                Snackbar.LENGTH_LONG
                            ).show()
                        is ForgotPasswordUiState.Loading ->
                            Snackbar.make(
                                binding.root,
                                if (state.isLoading) "Loading" else "Not Loading",
                                Snackbar.LENGTH_LONG
                            ).show()
                        ForgotPasswordUiState.Success -> findNavController().navigate(
                            ForgotPasswordFragmentDirections.toResetPasswordFragment(emailAddress)
                        )
                    }
                }
            }
        }
    }

    private fun setUpInputListener() {
        binding.apply {
            emailAddressInput.editText?.doAfterTextChanged {
                emailAddress = if (it.isNullOrEmpty()) "" else it.toString()
                emailAddressInput.error = null
                submitAction.isEnabled = emailAddress.isNotEmpty()
            }
        }
    }

    private fun removeInputListener() {
        binding.apply {
            emailAddressInput.editText?.doAfterTextChanged {}
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
