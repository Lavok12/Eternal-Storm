package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class IronOreTileType() : AbstractTileType() {
    override val tag: String = TilesList.iron_ore_block
    override val blockStrength: Int = 70
    override val maxHp: Int = 35
    override val texture: String = AppState.res("iron_ore_texture.png")
    override val drop: DropEntry = SingleDrop(ItemsList.iron_ore_block)
    override val tags = setOf(BlockTags.ORE, BlockTags.SOLID)
    override fun canBePolluted(): Boolean = false
}
