package la.vok.Game.GameContent.Tiles.System

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

abstract class AbstractTileType {

    open val tag: String = ""
    open val blockStrength: Int = 0
    open val maxHp: Int = 0
    open val texture: String = ""
    open val consumed: Boolean = true

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
        gameController: GameController
    ) {
        lg.stroke(0f)
        lg.strokeWeight(3f)
        lg.fill(0f, 0f)
        lg.setBlock(position, size-3f)
        lg.noStroke()
    }

    open fun place(x: Int, y: Int, item: Item) {}

    open fun damage(
        x: Int,
        y: Int,
        damage: Int,
        tileContext: TileContext,
    ) {
    }

    open fun onMined(
        x: Int,
        y: Int,
        mineData: MineData,
        tileContext: TileContext,
    ) {
    }

    open fun onRemoved(
        x: Int,
        y: Int,
        tileContext: TileContext,
        reason: Any? = null
    ) {
    }
}