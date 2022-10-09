package xml.one.pass.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import xml.one.pass.data.local.entity.AccountEntity

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(accountEntity: AccountEntity)

    @Query("UPDATE account SET name = :name WHERE id =:id")
    suspend fun updateAccountName(name: String, id: Int): Int

    @Query("UPDATE account SET password = :password WHERE id =:id")
    suspend fun updateAccountPassword(password: String, id: Int): Int

    @Query("SELECT * FROM account")
    suspend fun loadAccount(): AccountEntity?

    @Query("SELECT COUNT(id) FROM account")
    fun areThereAccounts(): Int

    @Query("SELECT COUNT(id) FROM account WHERE email =:email")
    fun doesAccountExistWithEmail(email: String): Int

    @Query("SELECT COUNT(id) FROM account WHERE email =:email AND password =:password")
    fun doesAccountExistWithEmailAndPassword(email: String, password: String): Int

    @Query("DELETE FROM account")
    suspend fun deleteAccount()
}
