package la.vok.Core.CoreContent.Input

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.CoreControllers.CoreController
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState
import processing.event.MouseEvent

class MouseInput(var coreController: CoreController) : Controller {
    init {
        create()
    }
    override fun logicalTick() {
        super.superTick()

        updateFrameStates()
        updatePosition()

        if (leftDown)  leftUpdate()
        if (rightDown) rightUpdate()
        if (centerDown) centerUpdate()
    }
    var screenPosition = Vec2(0f)
    var canvasPosition = Vec2(0f)
    var logicalPosition = Vec2(0f)

    var deltaScreenPosition = Vec2(0f)
    var deltaCanvasPosition = Vec2(0f)
    var deltaLogicalPosition = Vec2(0f)

    var leftDown = false
    var rightDown = false
    var centerDown = false

    var leftPressedFrame = false
    var rightPressedFrame = false
    var centerPressedFrame = false

    var leftReleasedFrame = false
    var rightReleasedFrame = false
    var centerReleasedFrame = false

    var leftLastClickTime = 0L
    var rightLastClickTime = 0L
    var centerLastClickTime = 0L

    // drag flags
    var leftDrag = false
    var rightDrag = false
    var centerDrag = false

    // drag start positions
    var dragStartLeft = Vec2(0f)
    var dragStartRight = Vec2(0f)
    var dragStartCenter = Vec2(0f)

    // drag start triggers
    var leftDragStarted = false
    var rightDragStarted = false
    var centerDragStarted = false

    var prevScreen = screenPosition
    var prevCanvas = canvasPosition
    var prevLogical = logicalPosition

    fun updatePosition() {
        coreController.mouseInputProcessing.mouseStartFrame()

        prevScreen = screenPosition
        prevCanvas = canvasPosition
        prevLogical = logicalPosition
        
        screenPosition = Vec2(AppState.main.mouseX.toFloat(), AppState.main.mouseY.toFloat())
        canvasPosition = AppState.lg.screenToCanvas(screenPosition)
        logicalPosition = AppState.lg.screenToLogical(screenPosition)

        deltaScreenPosition  = screenPosition  - prevScreen
        deltaCanvasPosition  = canvasPosition  - prevCanvas
        deltaLogicalPosition = logicalPosition - prevLogical

        coreController.mouseInputProcessing.mouseUpdate(logicalPosition, prevLogical)
        if (deltaLogicalPosition.length() > 0f) {
            coreController.mouseInputProcessing.mouseMove(logicalPosition, prevLogical)
        }
    }

    fun updateFrameStates() {
        leftPressedFrame = false
        rightPressedFrame = false
        centerPressedFrame = false

        leftReleasedFrame = false
        rightReleasedFrame = false
        centerReleasedFrame = false
    }


    // ================================
    //            PRESS
    // ================================
    fun leftPressed() {
        leftDown = true
        leftPressedFrame = true

        val now = System.currentTimeMillis()
        if (now - leftLastClickTime <= AppState.doubleClickDelay)
            coreController.mouseInputProcessing.leftDoubleClick(logicalPosition)
        leftLastClickTime = now

        dragStartLeft = logicalPosition.copy()
        leftDrag = true
        leftDragStarted = false  // dragStart ещё НЕ вызван!

        coreController.mouseInputProcessing.leftPressed(logicalPosition)
    }

    fun rightPressed() {
        rightDown = true
        rightPressedFrame = true

        val now = System.currentTimeMillis()
        if (now - rightLastClickTime <= AppState.doubleClickDelay)
            coreController.mouseInputProcessing.rightDoubleClick(logicalPosition)
        rightLastClickTime = now

        dragStartRight = logicalPosition.copy()
        rightDrag = true
        rightDragStarted = false

        coreController.mouseInputProcessing.rightPressed(logicalPosition)
    }

    fun centerPressed() {
        centerDown = true
        centerPressedFrame = true

        val now = System.currentTimeMillis()
        if (now - centerLastClickTime <= AppState.doubleClickDelay)
            coreController.mouseInputProcessing.centerDoubleClick(logicalPosition)
        centerLastClickTime = now

        dragStartCenter = logicalPosition.copy()
        centerDrag = true
        centerDragStarted = false

        coreController.mouseInputProcessing.centerPressed(logicalPosition)
    }


    // ================================
    //            RELEASE
    // ================================
    fun leftReleased() {
        leftDown = false
        leftReleasedFrame = true

        if (leftDrag) {
            leftDrag = false
            if (leftDragStarted)
                coreController.mouseInputProcessing.leftDragEnd(logicalPosition)
        }

        coreController.mouseInputProcessing.leftReleased(logicalPosition)
    }

    fun rightReleased() {
        rightDown = false
        rightReleasedFrame = true

        if (rightDrag) {
            rightDrag = false
            if (rightDragStarted)
                coreController.mouseInputProcessing.rightDragEnd(logicalPosition)
        }

        coreController.mouseInputProcessing.rightReleased(logicalPosition)
    }

    fun centerReleased() {
        centerDown = false
        centerReleasedFrame = true

        if (centerDrag) {
            centerDrag = false
            if (centerDragStarted)
                coreController.mouseInputProcessing.centerDragEnd(logicalPosition)
        }

        coreController.mouseInputProcessing.centerReleased(logicalPosition)
    }


    // ================================
    //            UPDATE
    // ================================
    fun leftUpdate() {
        coreController.mouseInputProcessing.leftUpdate(logicalPosition, prevLogical)

        if (leftDrag) {
            // first movement → dragStart
            if (!leftDragStarted && deltaLogicalPosition.length() > 0f) {
                leftDragStarted = true
                coreController.mouseInputProcessing.leftDragStart(dragStartLeft)
            }

            if (leftDragStarted && deltaLogicalPosition.length() > 0f)
                coreController.mouseInputProcessing.leftDrag(logicalPosition, prevLogical, dragStartLeft)
        }
    }

    fun rightUpdate() {
        coreController.mouseInputProcessing.rightUpdate(logicalPosition, prevLogical)

        if (rightDrag) {
            if (!rightDragStarted && deltaLogicalPosition.length() > 0f) {
                rightDragStarted = true
                coreController.mouseInputProcessing.rightDragStart(dragStartRight)
            }

            if (rightDragStarted && deltaLogicalPosition.length() > 0f)
                coreController.mouseInputProcessing.rightDrag(logicalPosition, prevLogical, dragStartRight)
        }
    }

    fun centerUpdate() {
        coreController.mouseInputProcessing.centerUpdate(logicalPosition, prevLogical)

        if (centerDrag) {
            if (!centerDragStarted && deltaLogicalPosition.length() > 0f) {
                centerDragStarted = true
                coreController.mouseInputProcessing.centerDragStart(dragStartCenter)
            }

            if (centerDragStarted && deltaLogicalPosition.length() > 0f)
                coreController.mouseInputProcessing.centerDrag(logicalPosition, prevLogical, dragStartCenter)
        }
    }


    fun mouseWheel(event: MouseEvent) {
        coreController.mouseInputProcessing.mouseWheel(logicalPosition, event)
    }
}
