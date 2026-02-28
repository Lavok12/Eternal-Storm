package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.TilesList

class DirtTileType() : AbstractTileType() {
    override val tag: String = TilesList.dirt_block
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = "dirtTexture.jpg"
    override val baseDrop: String = ItemsList.dirt_block
}