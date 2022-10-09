package xml.one.pass.data.local.mapper

import xml.one.pass.data.local.entity.AccountEntity
import xml.one.pass.domain.model.AccountModel

fun AccountEntity.mapToAccountModel(): AccountModel {
    return AccountModel(
        id = id,
        name = name,
        email = email,
        password = password,
        passphrase = passphrase
    )
}
