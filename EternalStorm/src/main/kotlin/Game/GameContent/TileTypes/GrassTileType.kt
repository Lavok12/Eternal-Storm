package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.State.AppState

class GrassTileType() : AbstractTileType() {
    override val tag: String = TilesList.grass_block
    override val blockStrength: Int = 10
    override val maxHp: Int = 12
    override val texture: String = AppState.res("grassTexture.jpg")
    override val drop: DropEntry = SingleDrop(ItemsList.grass_block)
}