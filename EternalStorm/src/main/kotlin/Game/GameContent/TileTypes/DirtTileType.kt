package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList

class DirtTileType() : AbstractTileType() {
    override val tag: String = TilesList.dirt_block
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = "dirtTexture.jpg"
    override val drop: DropEntry = SingleDrop(ItemsList.dirt_block)
}