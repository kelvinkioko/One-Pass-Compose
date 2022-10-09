package xml.one.pass.presentation.password.manage.edit

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import xml.one.pass.R
import xml.one.pass.presentation.password.manage.BasePasswordManagerFragment

@AndroidEntryPoint
class EditPasswordFragment : BasePasswordManagerFragment(
    buttonTitle = R.string.save_changes,
    pageTitle = R.string.update
) {

    private val args: EditPasswordFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setPassword(passwordId = args.passwordId)
    }
}
