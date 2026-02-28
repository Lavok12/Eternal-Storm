package la.vok.Core.CoreControllers

import la.vok.Core.CoreContent.Input.Interfaces.MouseInputInterface
import la.vok.LavokLibrary.Vectors.Vec2
import processing.event.MouseEvent
import la.vok.LLibs.Logger.ConsoleLogger
import la.vok.LLibs.Logger.LogLevel
import la.vok.Core.CoreControllers.Intergaces.Controller

class MouseInputProcessing(var coreController: CoreController) : MouseInputInterface, Controller {

    private val logger = ConsoleLogger("MouseInputProcessing", LogLevel.INFO)

    init {
        create()
    }

    override var startLeftClick: Vec2 = Vec2(0f)
    override var startRightClick: Vec2 = Vec2(0f)
    override var startCenterClick: Vec2 = Vec2(0f)

    val leftDown: Boolean
        get() = coreController.mouseInput.leftDown
    val rightDown: Boolean
        get() = coreController.mouseInput.rightDown
    val centerDown: Boolean
        get() = coreController.mouseInput.centerDown

    val wm: WindowsManager
        get() = coreController.windowsManager

    override fun mouseStartFrame() {
        wm.call { mouseStartFrame() }
    }

    override fun leftPressed(position: Vec2) {
        val window = wm.findWindowAt(position)
        logger.debug("Left Pressed at $position. Target window: ${window?.javaClass?.simpleName ?: "None"}")

        wm.leftClickWindow = window
        wm.changeActiveWindow(window)
        startLeftClick = position
        window?.startLeftClick = position

        wm.callOnce(position) { leftPressed(toWindowLogicalPos(position)) }
        wm.callOnce(position) { buttonsLeftPressed(toWindowLogicalPos(position)) }
        wm.call(wm.leftClickWindow) { leftPressedCaptured(toWindowLogicalPos(position)) }
    }

    override fun rightPressed(position: Vec2) {
        val window = wm.findWindowAt(position)
        logger.debug("Right Pressed at $position. Target window: ${window?.javaClass?.simpleName ?: "None"}")

        wm.rightClickWindow = window
        startRightClick = position
        window?.startRightClick = position

        wm.callOnce(position) { rightPressed(toWindowLogicalPos(position)) }
        wm.callOnce(position) { buttonsRightPressed(toWindowLogicalPos(position)) }
        wm.call(wm.rightClickWindow) { rightPressedCaptured(toWindowLogicalPos(position)) }
    }

    override fun centerPressed(position: Vec2) {
        val window = wm.findWindowAt(position)
        logger.debug("Center Pressed at $position. Target window: ${window?.javaClass?.simpleName ?: "None"}")

        wm.centerClickWindow = window
        startCenterClick = position
        window?.startCenterClick = position

        wm.callOnce(position) { centerPressed(toWindowLogicalPos(position)) }
        wm.call(wm.centerClickWindow) { centerPressedCaptured(toWindowLogicalPos(position)) }
    }

    override fun leftReleased(position: Vec2) {
        logger.debug("Left Released at $position")
        wm.callOnce(position) { leftReleased(toWindowLogicalPos(position)) }
        wm.call(wm.leftClickWindow) { leftReleasedCaptured(toWindowLogicalPos(position)) }
        wm.leftClickWindow = null
    }

    override fun rightReleased(position: Vec2) {
        logger.debug("Right Released at $position")
        wm.callOnce(position) { rightReleased(toWindowLogicalPos(position)) }
        wm.call(wm.rightClickWindow) { rightReleasedCaptured(toWindowLogicalPos(position)) }
        wm.rightClickWindow = null
    }

    override fun centerReleased(position: Vec2) {
        logger.debug("Center Released at $position")
        wm.callOnce(position) { centerReleased(toWindowLogicalPos(position)) }
        wm.call(wm.centerClickWindow) { centerReleasedCaptured(toWindowLogicalPos(position)) }
        wm.centerClickWindow = null
    }

    override fun leftUpdate(position: Vec2, oldPosition: Vec2) {
        wm.callOnce(position) {
            leftUpdate(toWindowLogicalPos(position), toWindowLogicalPos(oldPosition))
        }
        wm.call(wm.leftClickWindow) {
            leftUpdateCaptured(
                toWindowLogicalPos(position),
                toWindowLogicalPos(oldPosition)
            )
        }
    }

    override fun rightUpdate(position: Vec2, oldPosition: Vec2) {
        wm.callOnce(position) {
            rightUpdate(toWindowLogicalPos(position), toWindowLogicalPos(oldPosition))
        }
        wm.call(wm.rightClickWindow) {
            rightUpdateCaptured(
                toWindowLogicalPos(position),
                toWindowLogicalPos(oldPosition)
            )
        }
    }

    override fun centerUpdate(position: Vec2, oldPosition: Vec2) {
        wm.callOnce(position) {
            centerUpdate(toWindowLogicalPos(position), toWindowLogicalPos(oldPosition))
        }
        wm.call(wm.centerClickWindow) {
            centerUpdateCaptured(
                toWindowLogicalPos(position),
                toWindowLogicalPos(oldPosition)
            )
        }
    }

    override fun leftDoubleClick(position: Vec2) {
        logger.debug("Left Double Click at $position")
        wm.callOnce(position) { leftDoubleClick(toWindowLogicalPos(position)) }
    }

    override fun rightDoubleClick(position: Vec2) {
        logger.debug("Right Double Click at $position")
        wm.callOnce(position) { rightDoubleClick(toWindowLogicalPos(position)) }
    }

    override fun centerDoubleClick(position: Vec2) {
        logger.debug("Center Double Click at $position")
        wm.callOnce(position) { centerDoubleClick(toWindowLogicalPos(position)) }
    }

    override fun leftDragStart(position: Vec2) {
        logger.debug("Left Drag Start at $position")
        wm.callOnce(position) { leftDragStart(toWindowLogicalPos(position)) }
        wm.call(wm.leftClickWindow) { leftDragStartCaptured(toWindowLogicalPos(position)) }
    }

    override fun rightDragStart(position: Vec2) {
        logger.debug("Right Drag Start at $position")
        wm.callOnce(position) { rightDragStart(toWindowLogicalPos(position)) }
        wm.call(wm.rightClickWindow) { rightDragStartCaptured(toWindowLogicalPos(position)) }
    }

    override fun centerDragStart(position: Vec2) {
        logger.debug("Center Drag Start at $position")
        wm.callOnce(position) {
            centerDragStart(toWindowLogicalPos(position)) }
        wm.call(wm.centerClickWindow) { centerDragStartCaptured(toWindowLogicalPos(position)) }
    }

    override fun leftDrag(position: Vec2, oldPosition: Vec2, start: Vec2) {
        wm.callOnce(position) {
            leftDrag(
                toWindowLogicalPos(position),
                toWindowLogicalPos(oldPosition),
                toWindowLogicalPos(start)
            )
        }
        wm.call(wm.leftClickWindow) {
            leftDragCaptured(
                toWindowLogicalPos(position),
                toWindowLogicalPos(oldPosition),
                toWindowLogicalPos(start)
            )
        }
    }

    override fun rightDrag(position: Vec2, oldPosition: Vec2, start: Vec2) {
        wm.callOnce(position) {
            rightDrag(
                toWindowLogicalPos(position),
                toWindowLogicalPos(oldPosition),
                toWindowLogicalPos(start)
            )
        }
        wm.call(wm.rightClickWindow) {
            rightDragCaptured(
                toWindowLogicalPos(position),
                toWindowLogicalPos(oldPosition),
                toWindowLogicalPos(start)
            )
        }
    }

    override fun centerDrag(position: Vec2, oldPosition: Vec2, start: Vec2) {
        wm.callOnce(position) {
            centerDrag(
                toWindowLogicalPos(position),
                toWindowLogicalPos(oldPosition),
                toWindowLogicalPos(start)
            )
        }
        wm.call(wm.centerClickWindow) {
            centerDragCaptured(
                toWindowLogicalPos(position),
                toWindowLogicalPos(oldPosition),
                toWindowLogicalPos(start)
            )
        }
    }

    override fun leftDragEnd(position: Vec2) {
        logger.debug("Left Drag End at $position")
        wm.callOnce(position) { leftDragEnd(toWindowLogicalPos(position)) }
        wm.call(wm.leftClickWindow) { leftDragEndCaptured(toWindowLogicalPos(position)) }
    }

    override fun rightDragEnd(position: Vec2) {
        logger.debug("Right Drag End at $position")
        wm.callOnce(position) { rightDragEnd(toWindowLogicalPos(position)) }
        wm.call(wm.rightClickWindow) { rightDragEndCaptured(toWindowLogicalPos(position)) }
    }

    override fun centerDragEnd(position: Vec2) {
        logger.debug("Center Drag End at $position")
        wm.callOnce(position) { centerDragEnd(toWindowLogicalPos(position)) }
        wm.call(wm.centerClickWindow) { centerDragEndCaptured(toWindowLogicalPos(position)) }
    }

    override fun mouseWheel(position: Vec2, event: MouseEvent) {
        logger.trace("Mouse Wheel event at $position, count: ${event.count}")
        val window = wm.findWindowAt(position)
        wm.centerClickWindow = window

        wm.callOnce(position) { mouseWheel(toWindowLogicalPos(position), event) }
        if (true) { wm.call(wm.leftClickWindow) { mouseWheelCaptured(toWindowLogicalPos(position), event) }}
        if (true) { wm.call(wm.rightClickWindow) { mouseWheelCaptured(toWindowLogicalPos(position), event) }}
        if (true) { wm.call(wm.centerClickWindow) { mouseWheelCaptured(toWindowLogicalPos(position), event) }}
    }

    override fun mouseUpdate(position: Vec2, oldPosition: Vec2) {
        wm.callOnce(position) { mouseUpdate(toWindowLogicalPos(position), oldPosition) }
        wm.call { buttonsMouseInsideFalse() }
        wm.callOnce(position) { buttonsMouseInside(toWindowLogicalPos(position)) }
    }

    override fun mouseMove(position: Vec2, oldPosition: Vec2) {
        wm.callOnce(position) {
            mouseMove(toWindowLogicalPos(position), toWindowLogicalPos(oldPosition))
        }
        wm.onOffCursor(position)
    }
}