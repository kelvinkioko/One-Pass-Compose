package xml.one.pass.data.preference

import xml.one.pass.domain.preference.OnePassRepository

class OnePassRepositoryTestImpl : OnePassRepository {

    private var isLoggedIn: Boolean = false

    override suspend fun setLoginStatus(isLoggedIn: Boolean) {
        this.isLoggedIn = isLoggedIn
    }

    override suspend fun getLoginStatus(): Result<Boolean> {
        return Result.runCatching { isLoggedIn }
    }
}
