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
import xml.one.pass.data.local.entity.PasswordEntity
import xml.one.pass.extension.getCurrentDate

@RunWith(AndroidJUnit4::class)
@SmallTest
class PasswordDaoTests {

    private lateinit var onePassDatabase: OnePassDatabase
    private lateinit var passwordDao: PasswordDao
    private val passwordEntity = PasswordEntity(
        id = 1,
        siteName = "Facebook",
        url = "www.facebook.com",
        userName = "username",
        email = "username@email.com",
        password = "123456",
        phoneNumber = "+254700000000",
        securityQuestions = "",
        timeCreated = getCurrentDate(),
        timeUpdated = getCurrentDate()
    )

    private val passwordUpdateEntity = PasswordEntity(
        id = 1,
        siteName = "Facebook",
        url = "www.facebook.com",
        userName = "nameuser",
        email = "nameuser@email.com",
        password = "123456",
        phoneNumber = "+254700000000",
        securityQuestions = "",
        timeCreated = passwordEntity.timeCreated,
        timeUpdated = getCurrentDate()
    )

    @Before
    fun setup() {
        onePassDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            OnePassDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        passwordDao = onePassDatabase.passwordDao()
    }

    @Test
    fun insertPassword() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)

            val passwords = passwordDao.loadPassword()

            assertThat(passwords).contains(passwordEntity)
        }
    }

    @Test
    fun updatePasswordDetailsByConflict() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)
            passwordDao.insertPassword(passwordEntity = passwordUpdateEntity)

            val passwords = passwordDao.loadPassword()

            assertThat(passwords).doesNotContain(passwordEntity)
            assertThat(passwords).contains(passwordUpdateEntity)
        }
    }

    @Test
    fun updatePasswordDetails() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)
            passwordDao.updatePasswordDetails(
                id = 1,
                siteName = "Facebook",
                url = "www.facebook.com",
                userName = "nameuser",
                email = "nameuser@email.com",
                password = "123456",
                phoneNumber = "+254700000000",
                securityQuestions = "",
                timeUpdated = getCurrentDate()
            )

            val passwords = passwordDao.loadPassword()

            assertThat(passwords).doesNotContain(passwordEntity)
            assertThat(passwords).contains(passwordUpdateEntity)
        }
    }

    @Test
    fun loadPassword() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)

            val passwords = passwordDao.loadPassword()

            assertThat(passwords).contains(passwordEntity)
        }
    }

    @Test
    fun loadPasswordById() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)

            val passwords = passwordDao.loadPasswordById(passwordId = 1)

            assertThat(passwords).isEqualTo(passwordEntity)
        }
    }

    @Test
    fun doesPasswordExistWithID() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)

            val passwords = passwordDao.doesPasswordExistWithID(passwordId = 1)

            assertThat(passwords).isEqualTo(1)
        }
    }

    @Test
    fun passwordDoesNotExistWithID() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)

            val passwords = passwordDao.doesPasswordExistWithID(passwordId = 0)

            assertThat(passwords).isEqualTo(0)
        }
    }

    @Test
    fun passwordDoesNotExist() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)

            val passwords = passwordDao.doesPasswordExist(
                siteName = "Facebook",
                userName = "nameuser",
                email = "nameuser@email.com",
                phoneNumber = "+254720000000",
                password = "123456"
            )
            assertThat(passwords).isEqualTo(0)
        }
    }

    @Test
    fun doesPasswordExist() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)

            val passwords = passwordDao.doesPasswordExist(
                siteName = "Facebook",
                userName = "username",
                email = "username@email.com",
                phoneNumber = "+254700000000",
                password = "123456"
            )
            assertThat(passwords).isEqualTo(1)
        }
    }

    @Test
    fun deletePasswordByID() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)
            passwordDao.deletePasswordByID(id = 1)

            val passwords = passwordDao.loadPassword()
            assertThat(passwords).isEmpty()
        }
    }

    @Test
    fun deletePassword() {
        runBlocking {
            passwordDao.insertPassword(passwordEntity = passwordEntity)
            passwordDao.deletePassword()

            val passwords = passwordDao.loadPassword()
            assertThat(passwords).isEmpty()
        }
    }

    @After
    fun tearDown() {
        onePassDatabase.close()
    }
}
