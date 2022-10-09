package xml.one.pass.domain.repository

import xml.one.pass.domain.model.AccountModel

interface AccountRepository {
    suspend fun insertAccount(name: String, email: String, password: String)

    suspend fun updateAccountName(name: String, id: Int): Int

    suspend fun updateAccountPassword(password: String, id: Int): Int

    suspend fun loadAccount(): AccountModel?

    suspend fun areThereAccounts(): Boolean

    suspend fun doesAccountExistWithEmail(email: String): Boolean

    suspend fun doesAccountExistWithEmailAndPassword(email: String, password: String): Boolean

    suspend fun deleteAccount()
}
