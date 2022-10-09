package xml.one.pass.domain.model

data class AccountModel(
    var id: Int = 0,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var passphrase: String = ""
)
