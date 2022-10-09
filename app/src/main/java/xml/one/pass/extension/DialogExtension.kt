package xml.one.pass.extension

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import xml.one.pass.R
import java.util.Objects

fun Dialog.displayDialog(
    @DrawableRes icon: Int? = null,
    headline: String,
    supportingText: String,
    dismissLabel: String? = null,
    isDecisionVisible: Boolean = false,
    decisionLabel: String? = null,
    onDismissCallBack: (() -> (Unit))? = null,
    onDecisionCallBack: (() -> (Unit))? = null
) {
    setCanceledOnTouchOutside(false)
    setCancelable(false)
    setContentView(R.layout.dialog_notification)

    Objects.requireNonNull<Window>(window).also {
        it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        it.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
    show()

    val iconIV: ImageView = findViewById(R.id.icon)
    val headlineTV: TextView = findViewById(R.id.headline)
    val supportingTextTV: TextView = findViewById(R.id.supportingText)
    val dismissAction: Button = findViewById(R.id.dismissAction)
    val decisionAction: Button = findViewById(R.id.decisionAction)

    icon?.let {
        val context: Context = iconIV.context
        iconIV.isVisible = true
        iconIV.setImageDrawable(VectorDrawableCompat.create(context.resources, icon, context.theme))
    }

    headlineTV.text = headline
    supportingTextTV.text = supportingText

    dismissLabel?.let { dismissAction.text = it }
    decisionLabel?.let { decisionAction.text = it }

    dismissAction.setOnClickListener {
        onDismissCallBack?.invoke()
        dismiss()
    }

    decisionAction.apply {
        isVisible = isDecisionVisible
        setOnClickListener {
            onDecisionCallBack?.invoke()
            dismiss()
        }
    }
}
