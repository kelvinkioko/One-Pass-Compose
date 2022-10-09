package xml.one.pass.extension

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Set the adapter and call [clearReference] extension function in one call.
 * Use this extension if the current Fragment is going to be REPLACED.
 * (When using fragmentTransaction.add is not necessary) the back stack.
 */
fun <VH : RecyclerView.ViewHolder> RecyclerView.setNullableAdapter(
    adapter: RecyclerView.Adapter<VH>
) {
    this.adapter = adapter
    this.clearReference()
}

/**
 * Remove the adapter after the view has been detached from window in order to prevent memory leaks.
 */
internal fun RecyclerView.clearReference() {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) {}

        override fun onViewDetachedFromWindow(v: View?) {
            this@clearReference.adapter = null
        }
    })
}
