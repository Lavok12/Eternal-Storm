package la.vok.Game.GameContent.Tiles.TileTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState

class StoneTileType() : AbstractTileType() {
    override val tag: String = AppState.addNamespace("stone")
    override val blockStrength: Int = 50
    override val maxHp: Int = 0

    override fun render(tileContext: TileContext, lg: LGraphics, position: Vec2, size: Vec2, gameController: GameController) {
        lg.setImage(gameController.coreController.spriteLoader.getValue("stoneTexture.jpg"), position, size)
    }
}