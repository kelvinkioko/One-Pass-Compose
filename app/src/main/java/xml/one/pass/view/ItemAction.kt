package xml.one.pass.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import xml.one.pass.R
import xml.one.pass.databinding.ItemActionBinding

class ItemAction @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val itemBinding =
        ItemActionBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.ItemAction).apply {
                setActionTitle(title = getString(R.styleable.ItemAction_ActionTitle).orEmpty())
                getDrawable(R.styleable.ItemAction_ActionIcon)?.let { icon -> setActionIcon(icon) }
                recycle()
            }
        }
    }

    private fun setActionTitle(title: String = "") {
        itemBinding.actionTitle.text = title
    }

    private fun setActionIcon(icon: Drawable) {
        itemBinding.actionIcon.setImageDrawable(icon)
    }
}
