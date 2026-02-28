package la.vok.Game.GameContent.Tiles.System

import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p

abstract class AbstractTileType {

    open val tag: String = ""
    open val blockStrength: Int = 0
    open val maxHp: Int = 0
    open val texture: String = ""
    open val consumed: Boolean = true
    open val baseDrop: String = ""

    open fun render(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        lg.setImage(
            gameController.coreController.spriteLoader.getValue(texture),
            position,
            size
        )
    }

    open fun renderHighlight(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameRender: GameRender
    ) {
        lg.stroke(0f)
        lg.strokeWeight(3f)
        lg.fill(0f, 0f)
        lg.setBlock(position, size-3f)
        lg.noStroke()
    }

    open fun place(x: Int, y: Int, item: Item, mapController: MapController) {}

    open fun damage(
        x: Int,
        y: Int,
        damage: Int,
        tileContext: TileContext,
        mapController: MapController
    ) {
    }

    open fun onMined(
        x: Int,
        y: Int,
        mineData: MineData,
        tileContext: TileContext, mapController: MapController
    ) {
        var drop = getBaseDrop(mapController)
        if (drop != null) {
            mapController.mapApi.gameCycle.itemsApi.spawnItemEntity(
                drop,
                mapController.mapApi.getTilePos(x p y),
                true
            )
        }
    }

    open fun getBaseDrop(mapController: MapController) : Item? {
        if (baseDrop == "") return null
        return mapController.gameCycle.itemsApi.getRegisteredItem(baseDrop, 1)
    }

    open fun onRemoved(
        x: Int,
        y: Int,
        tileContext: TileContext,
        reason: Any? = null
    ) {
    }
}