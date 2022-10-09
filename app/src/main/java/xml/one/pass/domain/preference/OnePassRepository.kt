package xml.one.pass.domain.preference

interface OnePassRepository {
    suspend fun setLoginStatus(isLoggedIn: Boolean)

    suspend fun getLoginStatus(): Result<Boolean>
}
