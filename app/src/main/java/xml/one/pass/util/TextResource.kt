package xml.one.pass.util

import android.content.Context
import androidx.annotation.StringRes

sealed class TextResource {
    data class DynamicString(val text: String) : TextResource()

    data class StringResource(@StringRes val resId: Int) : TextResource()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
        }
    }
}
