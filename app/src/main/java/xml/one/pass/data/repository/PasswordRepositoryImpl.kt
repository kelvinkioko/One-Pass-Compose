package xml.one.pass.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xml.one.pass.data.local.OnePassDatabase
import xml.one.pass.data.local.mapper.asPasswordEntity
import xml.one.pass.data.local.mapper.mapToPasswordModel
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import xml.one.pass.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordRepositoryImpl @Inject constructor(
    onePassDatabase: OnePassDatabase
) : PasswordRepository {

    private val passwordDao = onePassDatabase.passwordDao()

    override suspend fun insertPassword(
        passwordModel: PasswordModel
    ): Flow<Resource<Boolean>> = flow {
        val passwordExists = passwordDao.doesPasswordExist(
            siteName = passwordModel.siteName,
            userName = passwordModel.userName,
            email = passwordModel.email,
            phoneNumber = passwordModel.phoneNumber,
            password = passwordModel.password
        ) > 0

        if (passwordExists) {
            emit(Resource.Error(message = "Password exists!"))
        } else {
            passwordDao.insertPassword(passwordEntity = passwordModel.asPasswordEntity())

            emit(
                Resource.Success(
                    data = passwordDao.doesPasswordExist(
                        siteName = passwordModel.siteName,
                        userName = passwordModel.userName,
                        email = passwordModel.email,
                        phoneNumber = passwordModel.phoneNumber,
                        password = passwordModel.password
                    ) > 0
                )
            )
        }
    }

    override suspend fun updatePasswordDetails(
        passwordModel: PasswordModel
    ): Flow<Resource<Boolean>> = flow {
        val passwordExists = passwordDao.doesPasswordExistWithID(passwordId = passwordModel.id) == 1

        if (passwordExists) {
            passwordDao.insertPassword(passwordModel.asPasswordEntity())

            emit(
                Resource.Success(
                    data = passwordDao.doesPasswordExist(
                        siteName = passwordModel.siteName,
                        userName = passwordModel.userName,
                        email = passwordModel.email,
                        phoneNumber = passwordModel.phoneNumber,
                        password = passwordModel.password
                    ) > 0
                )
            )
        } else {
            emit(Resource.Error(message = "Password doesn't exist!"))
        }
    }

    override suspend fun loadPassword(): List<PasswordModel> {
        return passwordDao.loadPassword().map { it.mapToPasswordModel() }
    }

    override suspend fun loadPasswordById(passwordId: Int): Flow<Resource<PasswordModel>> = flow {
        try {
            val password = passwordDao.loadPasswordById(passwordId = passwordId)

            password?.let { passwordEntity ->
                emit(Resource.Success(data = passwordEntity.mapToPasswordModel()))
            } ?: kotlin.run {
                emit(Resource.Error(message = "Couldn't find the password details"))
            }
        } catch (exception: Throwable) {
            emit(Resource.Error(message = exception.message ?: "Couldn't find the password details"))
        }
    }

    override suspend fun doesPasswordExist(
        siteName: String,
        userName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Boolean {
        return passwordDao.doesPasswordExist(
            siteName = siteName,
            userName = userName,
            email = email,
            phoneNumber = phoneNumber,
            password = password
        ) > 1
    }

    override suspend fun deletePasswordByID(id: Int): Boolean {
        passwordDao.deletePasswordByID(id = id)

        return passwordDao.doesPasswordExistWithID(passwordId = id) == 0
    }

    override suspend fun deletePassword() {
        passwordDao.deletePassword()
    }
}
