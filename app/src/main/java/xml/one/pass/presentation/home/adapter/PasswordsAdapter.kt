package xml.one.pass.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xml.one.pass.databinding.ItemStoredPasswordBinding
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.extension.copyPassword

class PasswordsAdapter(
    private val passwordID: (Int) -> Unit
) : ListAdapter<PasswordModel, PasswordsAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStoredPasswordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(password = currentList[position])
    }

    inner class ViewHolder(
        val binding: ItemStoredPasswordBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(password: PasswordModel) {
            binding.apply {
                logoContainer.text = password.siteName[0].toString()

                storedPasswordsSubTitle.text = password.siteName
                storedPasswordsUsername.text = if (password.userName.isNotEmpty())
                    password.userName
                else if (password.email.isNotEmpty())
                    password.email
                else
                    password.phoneNumber

                itemView.setOnClickListener {
                    passwordID(password.id)
                }

                passwordCopyAction.setOnCheckedChangeListener { _, _ ->
                    password.password.copyPassword(context = passwordCopyAction.context)
                }
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<PasswordModel>() {
            override fun areItemsTheSame(
                oldItem: PasswordModel,
                newItem: PasswordModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PasswordModel,
                newItem: PasswordModel
            ): Boolean = oldItem == newItem
        }
    }
}
