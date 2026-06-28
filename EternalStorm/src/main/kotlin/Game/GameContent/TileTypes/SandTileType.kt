package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class SandTileType() : AbstractTileType() {
    override val tag: String = TilesList.sand_block
    override val blockStrength: Int = 8
    override val maxHp: Int = 8
    override val texture: String = AppState.res("sandTexture.jpg")
    override val drop: DropEntry = SingleDrop(ItemsList.sand_block)
    override val tags: Set<String> = emptySet()
    override val hasGravity: Boolean = true
}
