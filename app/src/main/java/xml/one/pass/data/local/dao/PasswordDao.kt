package xml.one.pass.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import xml.one.pass.data.local.entity.PasswordEntity

@Dao
interface PasswordDao {
    @Insert(entity = PasswordEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(passwordEntity: PasswordEntity)

    @Query(
        "UPDATE password SET " +
            "site_name = :siteName, " +
            "uniformResourceLocator = :url, " +
            "user_name = :userName, " +
            "email_address =:email, " +
            "password =:password, " +
            "phone_number =:phoneNumber, " +
            "security_questions =:securityQuestions, " +
            "time_updated =:timeUpdated " +
            "WHERE id =:id"
    ) suspend fun updatePasswordDetails(
        id: Int,
        siteName: String,
        url: String = "",
        userName: String = "",
        email: String,
        password: String,
        phoneNumber: String = "",
        securityQuestions: String = "",
        timeUpdated: String
    ): Int

    @Query("SELECT * FROM password")
    suspend fun loadPassword(): List<PasswordEntity>

    @Query("SELECT * FROM password WHERE id =:passwordId")
    suspend fun loadPasswordById(passwordId: Int): PasswordEntity?

    @Query("SELECT COUNT(id) FROM password WHERE id =:passwordId")
    suspend fun doesPasswordExistWithID(passwordId: Int): Int

    @Query(
        "SELECT COUNT(id) FROM password WHERE " +
            "site_name =:siteName AND " +
            "password =:password AND " +
            "(user_name =:userName OR " +
            "email_address =:email OR " +
            "phone_number =:phoneNumber)"
    )
    suspend fun doesPasswordExist(
        siteName: String,
        userName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Int

    @Query("DELETE FROM password WHERE id =:id")
    suspend fun deletePasswordByID(id: Int)

    @Query("DELETE FROM password")
    suspend fun deletePassword()
}
