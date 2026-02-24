package la.vok.LLibs.Logger

interface Logger {
    fun debug(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun trace(message: String)
    fun error(message: String, throwable: Throwable? = null)
}
