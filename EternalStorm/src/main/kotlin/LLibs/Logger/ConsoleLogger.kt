package la.vok.LLibs.Logger

import java.text.SimpleDateFormat
import java.util.Date

class ConsoleLogger(
    private val name: String,
    private val minLevel: LogLevel = LogLevel.DEBUG,
    private val useColors: Boolean = true
) : Logger {

    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS")

    override fun trace(message: String) = log(LogLevel.TRACE, message)
    override fun debug(message: String) = log(LogLevel.DEBUG, message)
    override fun info(message: String)  = log(LogLevel.INFO, message)
    override fun warn(message: String)  = log(LogLevel.WARN, message)
    override fun error(message: String, throwable: Throwable?) = log(LogLevel.ERROR, message, throwable)

    private fun log(level: LogLevel, message: String, throwable: Throwable? = null) {
        if (level.priority < minLevel.priority) return

        val time = timeFormat.format(Date())
        val prefix = "$time [${level.name}] $name - "

        val coloredPrefix = if (useColors) {
            when (level) {
                LogLevel.TRACE -> Ansi.PURPLE + prefix
                LogLevel.DEBUG -> Ansi.GRAY + prefix
                LogLevel.INFO  -> Ansi.BLUE + prefix
                LogLevel.WARN  -> Ansi.YELLOW + prefix
                LogLevel.ERROR -> Ansi.RED + prefix
            }
        } else prefix

        println(coloredPrefix + message + if (useColors) Ansi.RESET else "")

        throwable?.printStackTrace()
    }
}
