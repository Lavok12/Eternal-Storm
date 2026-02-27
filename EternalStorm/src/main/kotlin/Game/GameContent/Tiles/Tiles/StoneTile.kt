package la.vok.Game.GameContent.Tiles.Tiles

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Tiles.TileTypes.AbstractTileType
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

class StoneTile(tileType: AbstractTileType) : Tile(tileType) {
    override fun render(lg: LGraphics, pos: Vec2, size: Vec2, gameController: GameController) {
        lg.setImage(gameController.coreController.spriteLoader.getValue("stoneTexture.jpg"), pos, size)
    }
}