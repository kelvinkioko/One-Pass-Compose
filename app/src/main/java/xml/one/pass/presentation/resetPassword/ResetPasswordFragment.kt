package xml.one.pass.presentation.resetPassword

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
import kotlinx.coroutines.launch
import xml.one.pass.R
import xml.one.pass.databinding.ResetPasswordFragmentBinding
import xml.one.pass.extension.viewBinding

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(R.layout.reset_password_fragment) {

    private val binding by viewBinding(ResetPasswordFragmentBinding::bind)
    private val viewModel: ResetPasswordViewModel by viewModels()

    private var password: String = ""
    private var confirmPassword: String = ""

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
            resetAction.setOnClickListener {
                if (password != confirmPassword) {
                    confirmPasswordInput.error = getString(R.string.password_match_error)
                } else {
                    viewModel.resetPassword(
                        password = password
                    )
                }
            }
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resetUiState.collect { state ->
                    when (state) {
                        is ResetUiState.Error ->
                            Snackbar.make(
                                binding.root,
                                state.message.asString(context = requireContext()),
                                Snackbar.LENGTH_LONG
                            ).show()
                        is ResetUiState.Loading ->
                            Snackbar.make(
                                binding.root,
                                if (state.isLoading) "Loading" else "Not Loading",
                                Snackbar.LENGTH_LONG
                            ).show()
                        ResetUiState.Success -> findNavController().navigate(
                            ResetPasswordFragmentDirections.toLoginFragment()
                        )
                    }
                }
            }
        }
    }

    private fun setUpInputListener() {
        binding.apply {
            passwordInput.editText?.doAfterTextChanged {
                password = if (it.isNullOrEmpty()) "" else it.toString()
                resetAction.isEnabled = password.isNotEmpty() && confirmPassword.isNotEmpty()
            }
            confirmPasswordInput.editText?.doAfterTextChanged {
                confirmPassword = if (it.isNullOrEmpty()) "" else it.toString()
                confirmPasswordInput.error = null
                resetAction.isEnabled = password.isNotEmpty() && confirmPassword.isNotEmpty()
            }
        }
    }

    private fun removeInputListener() {
        binding.apply {
            passwordInput.editText?.doAfterTextChanged {}
            confirmPasswordInput.editText?.doAfterTextChanged {}
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
