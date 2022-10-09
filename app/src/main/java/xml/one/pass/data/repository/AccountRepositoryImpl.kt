package xml.one.pass.data.repository

import xml.one.pass.data.local.OnePassDatabase
import xml.one.pass.data.local.entity.AccountEntity
import xml.one.pass.data.local.mapper.mapToAccountModel
import xml.one.pass.domain.model.AccountModel
import xml.one.pass.domain.repository.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    onePassDatabase: OnePassDatabase
) : AccountRepository {

    private val accountDao = onePassDatabase.accountDao()

    override suspend fun insertAccount(name: String, email: String, password: String) {
        accountDao.insertAccount(
            accountEntity = AccountEntity(
                name = name,
                email = email,
                password = password
            )
        )
    }

    override suspend fun updateAccountName(name: String, id: Int): Int {
        return accountDao.updateAccountName(name = name, id = id)
    }

    override suspend fun updateAccountPassword(password: String, id: Int): Int {
        return accountDao.updateAccountPassword(password = password, id = id)
    }

    override suspend fun loadAccount(): AccountModel? {
        return accountDao.loadAccount()?.mapToAccountModel() ?: kotlin.run { null }
    }

    override suspend fun areThereAccounts(): Boolean =
        accountDao.areThereAccounts() > 0

    override suspend fun doesAccountExistWithEmail(email: String): Boolean =
        accountDao.doesAccountExistWithEmail(email = email) > 0

    override suspend fun doesAccountExistWithEmailAndPassword(
        email: String,
        password: String
    ): Boolean = accountDao.doesAccountExistWithEmailAndPassword(
        email = email,
        password = password
    ) > 0

    override suspend fun deleteAccount() {
        accountDao.deleteAccount()
    }
}
