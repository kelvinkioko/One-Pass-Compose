package xml.one.pass.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import xml.one.pass.data.local.dao.AccountDao
import xml.one.pass.data.local.dao.PasswordDao
import xml.one.pass.data.local.entity.AccountEntity
import xml.one.pass.data.local.entity.PasswordEntity

@Database(
    entities = [AccountEntity::class, PasswordEntity::class],
    version = 1
)
abstract class OnePassDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun passwordDao(): PasswordDao
}
