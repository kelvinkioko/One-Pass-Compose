package xml.one.pass.util

import android.os.Build
import xml.one.pass.extension.DateConstants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed class DateResource {
    data class StringDate(val stringDate: String) : DateResource()

    data class LocalDateTimeDate(val localDate: LocalDateTime) : DateResource()

    fun asDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            (this as LocalDateTimeDate).localDate.format(
                DateTimeFormatter.ofPattern(DateConstants.DAY_DMY_HM_HYPHEN)
            )
        else
            (this as StringDate).stringDate
    }
}
