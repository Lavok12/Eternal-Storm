package Game.GameContent.TileTypes

import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.TilesList

class GrassTileType() : AbstractTileType() {
    override val tag: String = TilesList.grass_block
    override val blockStrength: Int = 10
    override val maxHp: Int = 12
    override val texture: String = "grassTexture.jpg"
}