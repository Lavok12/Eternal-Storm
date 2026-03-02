package la.vok.Game.GameContent.Entities.Ai

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2

abstract class AbstractAI(var entity: Entity, var gameCycle: GameCycle) {
    open fun spawn() {}
    open fun physicUpdate() {}
    open fun die() {}
    open fun damage(damage: DamageData) {}

    abstract fun targetScreenPos(): Vec2

    abstract fun targetWorldPos(): Vec2

    abstract fun targetMapPos(): LPoint

    // ───────── SCREEN ─────────

    fun screenToWorld(screenPos: Vec2): Vec2 =
        gameCycle.gameController.mainCamera.toWorldPos(screenPos)

    fun screenToMap(screenPos: Vec2): LPoint =
        worldToMap(screenToWorld(screenPos))

// ───────── WORLD ─────────

    fun worldToScreen(worldPos: Vec2): Vec2 =
        gameCycle.gameController.mainCamera.useCamera(worldPos)

    fun worldToMap(worldPos: Vec2): LPoint =
        gameCycle.mapApi.getPointFromPos(worldPos)

// ───────── MAP (TILE) ─────────

    fun mapToWorld(mapPos: LPoint): Vec2 =
        gameCycle.mapApi.getTilePos(mapPos)

    fun mapToScreen(mapPos: LPoint): Vec2 =
        worldToScreen(mapToWorld(mapPos))
    fun entityWorldPos(): Vec2 =
        entity.position
    fun entityMapPos(): LPoint =
        worldToMap(entity.position)
}