package la.vok.Game.GameContent.HandItems

import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.Entities.EntityApi
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.Vec2

open class HandItem(
    var handItemComponent: HandItemComponent,
    open val descriptor: HandItemDescriptor = HandItemDescriptor(spriteName = "default.png")
) {
    val gameController: GameController get() = handItemComponent.gameController
    val gameRender: GameRender get() = gameController.gameRender
    val mapApi: MapApi get() = gameController.mapController.mapApi
    val entityApi: EntityApi get() = gameController.entityController.entityApi
    val entity: Entity = handItemComponent.entity

    open var handItemRender = HandItemRender(this, gameRender.gameObjects)

    var useStage = 0f
    var block = false

    fun toWorldPos(pos: Vec2): Vec2 = gameController.mainCamera.toWorldPos(pos)
    fun getHandPos(): Vec2 = entity.position + handItemComponent.deltaWithFacing
    fun getVisualHandPos(): Vec2 = gameController.mainCamera.useCamera(getHandPos())

    open fun leftPressed(pos: Vec2) {
        if (!block) block = true
    }
    open fun rightPressed(pos: Vec2) {}
    open fun leftUpdate(pos: Vec2, oldPosition: Vec2) {}
    open fun rightUpdate(pos: Vec2, oldPosition: Vec2) {}

    open fun show() { handItemRender.show() }
    open fun hide() { handItemRender.hide() }
    open fun activate() {}
    open fun deactivate() {}

    open fun physicUpdate() {
        if (block) {
            useStage += descriptor.useStageStep
            if (useStage > descriptor.useDuration) {
                block = false
                useStage = 0f
            }
        }
    }

    open fun renderUpdate() {
        handItemRender.ROI_pos = getHandPos()
        handItemRender.facing = entity.facing
        handItemRender.useStage = useStage.toFloat()
    }
}