package xml.one.pass.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import xml.one.pass.R

/**
 * Returns `String` if the specified range in this string is equal to the specified range in another string.
 * @param thisOffset the start offset in this string of the substring to compare.
 * @param other the string against a substring of which the comparison is performed.
 * @param otherOffset the start offset in the other string of the substring to compare.
 * @param length the length of the substring to compare.
 */
fun String.maskString(): String = replaceRange(0 until length, "*".repeat(length))

fun String.copyPassword(context: Context) {
    // Gets a handle to the clipboard service.
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // Creates a new text clip to put on the clipboard
    val clip: ClipData = ClipData.newPlainText(
        context.getString(R.string.clip_data_label), this
    )
    // Set the clipboard's primary clip.
    clipboard.setPrimaryClip(clip)
}
