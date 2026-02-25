package la.vok.Game.GameContent.Tiles.Tiles

import la.vok.Game.GameContent.Tiles.TileTypes.AbstractTileType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

class StoneTile(tileType: AbstractTileType) : Tile(tileType) {
    override fun render(lg: LGraphics, pos: Vec2, size: Vec2) {
        lg.setImage(coreController.spriteLoader.getValue("stoneTexture.jpg"), pos, size)
    }
}