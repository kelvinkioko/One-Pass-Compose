package xml.one.pass.presentation.password.manage.add

import dagger.hilt.android.AndroidEntryPoint
import xml.one.pass.R
import xml.one.pass.presentation.password.manage.BasePasswordManagerFragment

@AndroidEntryPoint
class AddPasswordFragment : BasePasswordManagerFragment(
    buttonTitle = R.string.add_password,
    pageTitle = R.string.add_new
)
