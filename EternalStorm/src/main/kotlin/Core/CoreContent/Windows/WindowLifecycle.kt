package la.vok.Core.CoreContent.Windows

interface WindowLifecycle {

    fun start()
    fun destroy()
    suspend fun threadUpdate()
    fun preUpdate()
    fun update()
    fun postUpdate()
    fun postRenderUpdate()
    fun blockedUpdate()
    fun windowBlocked() {}
    fun windowUnblocked() {}
}
