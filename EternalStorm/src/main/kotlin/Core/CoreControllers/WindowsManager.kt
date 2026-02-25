package la.vok.Core.CoreControllers

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import la.vok.Core.CoreContent.Windows.WindowLifecycle
import la.vok.Core.CoreContent.Windows.WindowsSnapshot
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState
import la.vok.Windows.Message.WindowMessage
import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel
import la.vok.Core.CoreControllers.Intergaces.Controller

class WindowsManager(var coreController: CoreController) : WindowLifecycle, Controller {

    private val logger = ConsoleLogger("WindowsManager", LogLevel.DEBUG)

    init {
        logger.info("Initializing WindowsManager")
        create()
    }

    var windowsList = ArrayList<AbstractWindow>()
    var destroyList = ArrayList<AbstractWindow>()
    var appendList = ArrayList<AbstractWindow>()

    var ids: Long = 0
    var indexMap = HashMap<Long, AbstractWindow>()
    var tagsMap = HashMap<String, HashSet<AbstractWindow>>()

    var messages = ArrayList<WindowMessage>()
    var activeWindow: AbstractWindow? = null

    var leftClickWindow: AbstractWindow? = null
    var rightClickWindow: AbstractWindow? = null
    var centerClickWindow: AbstractWindow? = null

    private val blockStack = ArrayDeque<AbstractWindow>()

    private val blockedByWindow: AbstractWindow?
        get() = blockStack.lastOrNull()

    fun getSnapshot() : WindowsSnapshot {
        logger.debug("Creating WindowsSnapshot. Windows: ${windowsList.size}, BlockStack: ${blockStack.size}")
        val newWindowsList = ArrayList<AbstractWindow>()
        val newDestroyList = ArrayList<AbstractWindow>()
        val newAppendList  = ArrayList<AbstractWindow>()
        val newBlockStack = ArrayDeque<AbstractWindow>()

        for (i in windowsList) {
            newWindowsList += i
        }
        for (i in destroyList) {
            newDestroyList += i
        }
        for (i in appendList) {
            newAppendList += i
        }
        for (i in blockStack) {
            newBlockStack += i
        }
        return WindowsSnapshot(newWindowsList, newDestroyList, newAppendList, newBlockStack)
    }

    private var ssnapshot: WindowsSnapshot? = null

    fun setSnapshot(snapshot: WindowsSnapshot) {
        logger.debug("Setting new snapshot for next tick")
        ssnapshot = snapshot
    }

    fun applySnapshot() {
        if (ssnapshot != null) {
            logger.info("Applying snapshot. Overwriting current state.")
            windowsList.clear()
            destroyList.clear()
            appendList.clear()
            blockStack.clear()

            for (i in ssnapshot!!.windowsList) {
                windowsList += i
            }
            for (i in ssnapshot!!.destroyList) {
                destroyList += i
            }
            for (i in ssnapshot!!.appendList) {
                appendList += i
            }
            for (i in ssnapshot!!.blockStack) {
                blockStack += i
            }
            ssnapshot = null
        }
    }

    fun onOffCursor(pos: Vec2) {
        var flag = false
        var window = findWindowAt(pos)
        if (window != null) {
            if (window.noCursor) {
                flag = true
            }
        }
        if (flag) {
            AppState.main.noCursor()
        } else {
            AppState.main.cursor()
        }
    }

    fun blockBy(window: AbstractWindow) {
        if (blockStack.lastOrNull() === window) return

        logger.info("Blocking interface by window: ${window.javaClass.simpleName} (ID: ${window.id})")
        blockStack += window

        windowsList.forEach {
            if (it !== window) it.windowBlocked()
        }
    }

    fun unblock(window: AbstractWindow) {
        val wasTop = blockedByWindow === window
        if (blockStack.remove(window)) {
            logger.info("Removing window ${window.javaClass.simpleName} (ID: ${window.id}) from block stack")
        }

        if (wasTop) {
            blockedByWindow?.let { top ->
                logger.debug("New top blocking window: ${top.javaClass.simpleName}")
                windowsList.forEach {
                    if (it !== top) it.windowBlocked()
                }
            } ?: run {
                logger.info("Block stack empty. Unblocking all windows.")
                call { windowUnblocked() }
            }
        }
    }

    fun unblockAll() {
        if (blockStack.isEmpty()) return
        logger.info("Clearing block stack. Unblocking all.")
        blockStack.clear()
        call { windowUnblocked() }
    }

    fun isBlocked(): Boolean =
        blockStack.isNotEmpty()

    private fun isAllowed(window: AbstractWindow?): Boolean =
        blockedByWindow == null || blockedByWindow === window

    fun findWindowAt(position: Vec2): AbstractWindow? {
        blockedByWindow?.let { blocker ->
            return if (blocker.inside(position)) blocker else null
        }

        return windowsList
            .asReversed()
            .firstOrNull { it.inside(position) }
    }

    fun setActiveWindow(position: Vec2) {
        activeWindow = findWindowAt(position)
    }

    fun changeActiveWindow(window: AbstractWindow?) {
        activeWindow = window
    }

    fun callForced(window: AbstractWindow?, action: AbstractWindow.() -> Unit) {
        window?.action()
    }

    fun callForced(action: AbstractWindow.() -> Unit) {
        windowsList.forEach { it.action() }
    }

    fun call(window: AbstractWindow?, action: AbstractWindow.() -> Unit) {
        if (isAllowed(window)) window?.action()
    }

    fun call(action: AbstractWindow.() -> Unit) {
        val blocked = blockedByWindow
        if (blocked != null) {
            blocked.action()
            return
        }

        for (i in windowsList.size - 1 downTo 0) {
            windowsList[i].action()
        }
    }

    fun callOnce(position: Vec2, action: AbstractWindow.() -> Unit) {
        findWindowAt(position)?.action()
    }

    fun addWindow(window: AbstractWindow): Long {
        window.id = ids
        logger.debug("Queueing window for addition: ${window.javaClass.simpleName} (ID: $ids)")
        appendList += window
        return ids++
    }

    fun destroyWindow(window: AbstractWindow) {
        logger.debug("Queueing window for destruction: ${window.javaClass.simpleName} (ID: ${window.id})")
        destroyList += window
    }

    fun bringToFront(window: AbstractWindow) {
        if (windowsList.remove(window)) {
            windowsList += window
        }
    }

    fun sendToBack(window: AbstractWindow) {
        if (windowsList.remove(window)) {
            windowsList.add(0, window)
        }
    }

    fun bringAbove(window: AbstractWindow, target: AbstractWindow) {
        if (window === target) return
        windowsList.remove(window)
        val idx = windowsList.indexOf(target)
        windowsList.add(if (idx >= 0) idx + 1 else windowsList.size, window)
    }

    fun sendMessage(windowMessage: WindowMessage) {
        logger.trace("Message received: ${windowMessage.toPrintString()}")
        messages += windowMessage
    }

    fun handleMessage(windowMessage: WindowMessage) {
        when (windowMessage.targetType()) {
            WindowMessage.TargetType.NONE ->
                windowsList.forEach { it.handleMessage(windowMessage) }

            WindowMessage.TargetType.ID ->
                indexMap[windowMessage.targetId]?.handleMessage(windowMessage)

            WindowMessage.TargetType.TAG ->
                tagsMap[windowMessage.targetTag]?.forEach {
                    it.handleMessage(windowMessage)
                }
        }
    }

    fun applyMessages() {
        var size = messages.size
        for (i in 0 until size) {
            handleMessage(messages[0])
            messages.removeAt(0)
        }
    }

    fun applyPendingAdds() {
        if (appendList.isNotEmpty()) {
            logger.debug("Applying ${appendList.size} pending adds")
        }
        appendList.forEach { window ->
            indexMap[window.id] = window

            window.tags.forEach { tag ->
                tagsMap.getOrPut(tag) { HashSet() }.add(window)
            }

            windowsList += window
            window.start()
        }
        appendList.clear()
    }

    fun applyPendingRemoves() {
        if (destroyList.isNotEmpty()) {
            logger.debug("Applying ${destroyList.size} pending removes")
        }
        destroyList.forEach { window ->
            unblock(window)

            if (activeWindow === window)
                activeWindow = null

            windowsList.remove(window)
            window.tags.forEach { tagsMap[it]?.remove(window) }
            indexMap.remove(window.id)
            window.destroy()
        }
        destroyList.clear()
    }

    fun setInsideWindow() {
        windowsList.forEach { it.mouseInside = false }

        findWindowAt(
            AppState.coreController.mouseInput.logicalPosition
        )?.mouseInside = true
    }

    override fun logicalTick() {
        super.superTick()

        applySnapshot()
        applyPendingAdds()
        setInsideWindow()
        applyMessages()
        applyPendingRemoves()

        if (isBlocked()) blockedUpdate()

        preUpdate()
        runBlocking { threadUpdate() }
        update()
        postUpdate()
    }

    override fun start() {}
    override fun destroy() {
        logger.info("Destroying WindowsManager and all windows")
        windowsList.forEach { it.destroy() }
    }

    override suspend fun threadUpdate() = coroutineScope {
        windowsList.forEach { w ->
            if (isAllowed(w)) {
                launch { w.threadUpdate() }
            }
        }
    }

    override fun blockedUpdate() {
        val top = blockedByWindow ?: return
        windowsList.forEach {
            if (it !== top) it.blockedUpdate()
        }
    }

    override fun preUpdate() = call { preUpdate() }
    override fun update() = call { update() }
    override fun postUpdate() = call { postUpdate() }
    override fun postRenderUpdate() = call { postRenderUpdate() }

    fun renderWindows(mainRender: MainRender) {
        call {
            baseRender(mainRender)
            render(mainRender)
            basePostRender(mainRender)
        }
    }

    fun useWindows(lg: LGraphics) {
        windowsList
            .filter { it !== blockedByWindow }
            .forEach {
                lg.setImage(it.lg.pg, it.windowPosition, it.windowSize)
            }
    }

    fun useBlockedByWindow(lg: LGraphics) {
        blockedByWindow?.let {
            lg.setImage(it.lg.pg, it.windowPosition, it.windowSize)
        }
    }

    fun hasWindow(id: Long): Boolean =
        indexMap.containsKey(id)

    fun getWindow(id: Long): AbstractWindow? =
        indexMap[id]

    fun getWindowsByTag(tag: String): Set<AbstractWindow> =
        tagsMap[tag] ?: emptySet()

    fun containsTag(tag: String): Boolean =
        tagsMap[tag]?.isNotEmpty() == true

    override fun physicTick() {
        physicPreUpdate()
        physicUpdate()
        physicPostUpdate()
    }

    fun physicPreUpdate() = call { physicPreUpdate() }
    fun physicUpdate() = call { physicUpdate() }
    fun physicPostUpdate() = call { physicPostUpdate() }
}