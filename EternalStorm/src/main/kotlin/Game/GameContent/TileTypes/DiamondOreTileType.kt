package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList

class DiamondOreTileType() : AbstractTileType() {
    override val tag: String = TilesList.diamond_ore_block
    override val blockStrength: Int = 100
    override val maxHp: Int = 50
    override val texture: String = "diamond_ore_texture.jpg"
    override val drop: DropEntry = SingleDrop(ItemsList.diamond_ore_block)
}
