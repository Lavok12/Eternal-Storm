package la.vok.Game.GameContent.Tiles.System

import la.vok.Core.GameControllers.GameController
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

abstract class AbstractTileType  {
    open val tag: String = ""
    open val blockStrength: Int = 0
    open val maxHp: Int = 0

    open fun render(tileContext: TileContext, lg: LGraphics, position: Vec2, size: Vec2, gameController: GameController) {

    }
}