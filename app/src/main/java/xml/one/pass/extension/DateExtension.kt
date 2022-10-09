package xml.one.pass.extension

import xml.one.pass.extension.DateConstants.YMD_T_HM_HYPHEN
import java.text.SimpleDateFormat
import java.util.Calendar

fun getCurrentDate(desiredFormat: String = YMD_T_HM_HYPHEN): String {
    val calendar = Calendar.getInstance()
    val outputFormat = SimpleDateFormat(desiredFormat)
    return outputFormat.format(calendar.time)
}

object DateConstants {
    const val YMD_HYPHEN = "yyyy-MM-dd"
    const val YMD_FORWARD_SLASH = "yyyy/MM/dd"
    const val DMY_HYPHEN = "dd-MM-yyyy"
    const val DMY_FORWARD_SLASH = "dd/MM/yyyy"
    const val YMD_HMS_HYPHEN = "yyyy-MM-dd HH:mm:ss"
    const val YMD_HMS_FORWARD_SLASH = "yyyy/MM/dd HH:mm:ss"
    const val MDY_HMS_A_FORWARD_SLASH = "MM/dd/yyyy HH:mm:ss a"
    const val YMD_T_HMS_HYPHEN = "yyyy-MM-dd'T'HH:mm:ss"
    const val YMD_T_HM_HYPHEN = "yyyy-MM-dd'T'HH:mm"
    const val DMY_HMS_HYPHEN = "dd-MM-yyyy HH:mm:ss"
    const val DAY_DMY_HM_HYPHEN = "EEE dd, MMM yyyy HH:mm"
}
