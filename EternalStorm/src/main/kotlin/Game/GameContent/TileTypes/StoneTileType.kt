package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList

class StoneTileType() : AbstractTileType() {
    override val tag: String = TilesList.stone_block
    override val blockStrength: Int = 50
    override val maxHp: Int = 25
    override val texture: String = "stoneTexture.jpg"
    override val drop: DropEntry = SingleDrop(ItemsList.stone_block)
}