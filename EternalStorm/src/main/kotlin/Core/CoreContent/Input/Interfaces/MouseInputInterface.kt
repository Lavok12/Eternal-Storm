package la.vok.Core.CoreContent.Input.Interfaces

import la.vok.LavokLibrary.Vectors.Vec2
import processing.event.MouseEvent

interface MouseInputInterface {
    var startLeftClick: Vec2
    var startRightClick: Vec2
    var startCenterClick: Vec2

    fun mouseStartFrame()

    fun leftPressed(position: Vec2)
    fun rightPressed(position: Vec2)
    fun centerPressed(position: Vec2)

    fun leftReleased(position: Vec2)
    fun rightReleased(position: Vec2)
    fun centerReleased(position: Vec2)

    fun leftUpdate(position: Vec2, oldPosition: Vec2)
    fun rightUpdate(position: Vec2, oldPosition: Vec2)
    fun centerUpdate(position: Vec2, oldPosition: Vec2)

    fun leftDoubleClick(position: Vec2)
    fun rightDoubleClick(position: Vec2)
    fun centerDoubleClick(position: Vec2)

    fun leftDragStart(position: Vec2)
    fun rightDragStart(position: Vec2)
    fun centerDragStart(position: Vec2)

    fun leftDrag(position: Vec2, oldPosition: Vec2, start: Vec2)
    fun rightDrag(position: Vec2, oldPosition: Vec2, start: Vec2)
    fun centerDrag(position: Vec2, oldPosition: Vec2, start: Vec2)

    fun leftDragEnd(position: Vec2)
    fun rightDragEnd(position: Vec2)
    fun centerDragEnd(position: Vec2)

    fun mouseWheel(position: Vec2, event: MouseEvent)

    fun mouseUpdate(position: Vec2, oldPosition: Vec2)
    fun mouseMove(position: Vec2, oldPosition: Vec2)
}