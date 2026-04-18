package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class GrassTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.grass_block
    override val texture: String = AppState.res("grassTexture.jpg")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.grass_block)
}