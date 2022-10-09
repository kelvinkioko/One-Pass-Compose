package xml.one.pass.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "site_name")
    val siteName: String = "",
    @ColumnInfo(name = "uniformResourceLocator")
    val url: String = "",
    @ColumnInfo(name = "user_name")
    val userName: String = "",
    @ColumnInfo(name = "email_address")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String = "",
    @ColumnInfo(name = "security_questions")
    val securityQuestions: String = "",
    @ColumnInfo(name = "time_created")
    val timeCreated: String = "",
    @ColumnInfo(name = "time_updated")
    val timeUpdated: String
)
