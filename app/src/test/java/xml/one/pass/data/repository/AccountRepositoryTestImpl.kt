package xml.one.pass.data.repository

import xml.one.pass.domain.model.AccountModel
import xml.one.pass.domain.repository.AccountRepository

class AccountRepositoryTestImpl : AccountRepository {

    private var accountModel = mutableListOf<AccountModel>()

    fun createTestAccount() {
        accountModel.add(
            AccountModel(
                id = 1,
                name = "Kelvin Kioko",
                email = "kiokokelvin@gmail.com",
                password = "12345678"
            )
        )
    }

    override suspend fun insertAccount(name: String, email: String, password: String) {
        accountModel.add(
            AccountModel(
                id = 1,
                name = name,
                email = email,
                password = password
            )
        )
    }

    override suspend fun updateAccountName(name: String, id: Int): Int {
        accountModel.find { it.id == id }.also { it?.name = name }

        return accountModel.count { it.id == id && it.name == name }
    }

    override suspend fun updateAccountPassword(password: String, id: Int): Int {
        accountModel.find { it.id == id }.also { it?.password = password }

        return accountModel.count { it.id == id && it.password == password }
    }

    override suspend fun loadAccount(): AccountModel? {
        return accountModel.firstOrNull()
    }

    override suspend fun areThereAccounts(): Boolean {
        return accountModel.isNotEmpty()
    }

    override suspend fun doesAccountExistWithEmail(email: String): Boolean {
        return accountModel.any { it.email == email }
    }

    override suspend fun doesAccountExistWithEmailAndPassword(
        email: String,
        password: String
    ): Boolean {
        return accountModel.any { it.email == email && it.password == password }
    }

    override suspend fun deleteAccount() {
        accountModel.clear()
    }
}
