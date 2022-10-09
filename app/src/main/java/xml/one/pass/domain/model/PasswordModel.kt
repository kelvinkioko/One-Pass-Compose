package xml.one.pass.domain.model

import xml.one.pass.util.DateResource

data class PasswordModel(
    var id: Int = 0,
    var siteName: String = "",
    var url: String = "",
    var userName: String = "",
    var email: String,
    var password: String,
    var phoneNumber: String = "",
    var securityQuestions: String = "",
    var timeCreated: DateResource,
    var timeUpdated: DateResource
)
