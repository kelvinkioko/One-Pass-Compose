package xml.one.pass.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import xml.one.pass.data.local.OnePassDatabase
import xml.one.pass.data.local.entity.AccountEntity

@RunWith(AndroidJUnit4::class)
@SmallTest
class AccountDaoTest {

    private lateinit var onePassDatabase: OnePassDatabase
    private lateinit var accountDao: AccountDao
    private val accountEntity = AccountEntity(
        name = "Kelvin Kioko",
        email = "kiokokelvin@gmail.com",
        password = "12345678"
    )

    @Before
    fun setup() {
        onePassDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            OnePassDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        accountDao = onePassDatabase.accountDao()
    }

    @Test
    fun insertAccount() {
        runBlocking {
            accountDao.insertAccount(accountEntity = accountEntity)

            val savedAccountDao = accountDao.loadAccount()

            assertThat(savedAccountDao).isEqualTo(accountEntity)
        }
    }

    @Test
    fun updateAccountName() {
        runBlocking {
            accountDao.insertAccount(accountEntity = accountEntity)
            accountDao.updateAccountName(name = "Olua Designer", id = 0)

            val savedAccountDao = accountDao.loadAccount()

            assertThat(savedAccountDao?.name).isEqualTo("Olua Designer")
        }
    }

    @Test
    fun updateAccountPassword() {
        runBlocking {
            accountDao.insertAccount(accountEntity = accountEntity)
            accountDao.updateAccountPassword(password = "87654321", id = 0)

            val savedAccountDao = accountDao.loadAccount()

            assertThat(savedAccountDao?.password).isEqualTo("87654321")
        }
    }

    @Test
    fun loadAccount() {
        runBlocking {
            accountDao.insertAccount(accountEntity = accountEntity)

            val savedAccountDao = accountDao.loadAccount()

            assertThat(savedAccountDao).isEqualTo(accountEntity)
        }
    }

    @Test
    fun testAccountsWhenAccountsDBNotEmpty() {
        runBlocking {
            accountDao.insertAccount(accountEntity = accountEntity)

            val accountsAvailable = accountDao.areThereAccounts()

            assertThat(accountsAvailable).isEqualTo(1)
        }
    }

    @Test
    fun testAccountsWhenAccountsDBEmpty() {
        runBlocking {
            val accountsAvailable = accountDao.areThereAccounts()

            assertThat(accountsAvailable).isEqualTo(0)
        }
    }

    @Test
    fun doesAccountExistWithEmail() {
        runBlocking {
            accountDao.insertAccount(accountEntity = accountEntity)

            val account = accountDao.doesAccountExistWithEmail("kiokokelvin@gmail.com")

            assertThat(account).isEqualTo(1)
        }
    }

    @Test
    fun doesAccountDoesNotExistWithEmail() {
        runBlocking {
            val account = accountDao.doesAccountExistWithEmail("kiokokelvin@gmail.com")

            assertThat(account).isEqualTo(0)
        }
    }

    @Test
    fun doesAccountExistWithEmailAndPassword() {
        runBlocking {
            accountDao.insertAccount(accountEntity = accountEntity)

            val account = accountDao.doesAccountExistWithEmailAndPassword(
                email = "kiokokelvin@gmail.com",
                password = "12345678"
            )

            assertThat(account).isEqualTo(1)
        }
    }

    @Test
    fun accountDoesNotExistWithEmailAndPassword() {
        runBlocking {
            accountDao.insertAccount(accountEntity = accountEntity)

            val account = accountDao.doesAccountExistWithEmailAndPassword(
                email = "kiokokelvin@gmail.com",
                password = "87654321"
            )
            assertThat(account).isEqualTo(0)
        }
    }

    @Test
    fun deleteAccount() {
        runBlocking {
            accountDao.insertAccount(accountEntity = accountEntity)
            accountDao.deleteAccount()

            val account = accountDao.loadAccount()
            assertThat(account).isNull()
        }
    }

    @After
    fun tearDown() {
        onePassDatabase.close()
    }
}
