package xml.one.pass.data.local.mapper

import android.os.Build
import xml.one.pass.data.local.entity.PasswordEntity
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.extension.DateConstants
import xml.one.pass.util.DateResource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun PasswordEntity.mapToPasswordModel(): PasswordModel {
    return PasswordModel(
        id = id,
        siteName = siteName,
        url = url,
        userName = userName,
        email = email,
        password = password,
        phoneNumber = phoneNumber,
        securityQuestions = securityQuestions,
        timeCreated = timeCreated.dateFormatter(),
        timeUpdated = timeUpdated.dateFormatter()
    )
}

fun PasswordModel.asPasswordEntity(): PasswordEntity {
    return PasswordEntity(
        id = id,
        siteName = siteName,
        url = url,
        userName = userName,
        email = email,
        password = password,
        phoneNumber = phoneNumber,
        securityQuestions = securityQuestions,
        timeCreated = timeCreated.dateFormatter(),
        timeUpdated = timeUpdated.dateFormatter()
    )
}

/**
 * Necessary because LocalDateTime is only available from API 26 onwards
 * we'd like to support API 21 onwards
 */
fun String.dateFormatter(): DateResource {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        DateResource.LocalDateTimeDate(
            localDate = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        )
    else
        DateResource.StringDate(stringDate = this)
}

fun DateResource.dateFormatter(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        (this as DateResource.LocalDateTimeDate).localDate.format(
            DateTimeFormatter.ofPattern(DateConstants.YMD_T_HM_HYPHEN)
        )
    else
        (this as DateResource.StringDate).stringDate
}
