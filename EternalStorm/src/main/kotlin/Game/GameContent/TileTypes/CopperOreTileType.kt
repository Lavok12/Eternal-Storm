package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class CopperOreTileType() : AbstractTileType() {
    override val tag: String = TilesList.copper_ore_block
    override val blockStrength: Int = 60
    override val maxHp: Int = 30
    override val texture: String = AppState.res("copper_ore_texture.png")
    override val drop: DropEntry = SingleDrop(ItemsList.raw_copper_item)
    override val tags = setOf(BlockTags.ORE, BlockTags.SOLID)
    override fun canBePolluted(): Boolean = false
}
