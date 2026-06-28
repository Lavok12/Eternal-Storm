package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class SandstoneTileType() : AbstractTileType() {
    override val tag: String = TilesList.sandstone_block
    override val blockStrength: Int = 12
    override val maxHp: Int = 12
    override val texture: String = AppState.res("sandstoneTexture.jpg")
    override val drop: DropEntry = SingleDrop(ItemsList.sandstone_block)
    override val tags: Set<String> = emptySet()
    override val hasGravity: Boolean = false
}
