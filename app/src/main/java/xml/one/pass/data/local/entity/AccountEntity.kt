package xml.one.pass.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "email")
    val email: String = "",
    @ColumnInfo(name = "password")
    val password: String = "",
    @ColumnInfo(name = "passphrase")
    val passphrase: String = ""
)
