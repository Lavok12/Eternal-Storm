package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class DirtTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.dirt_block
    override val texture: String = AppState.res("dirtTexture.jpg")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.dirt_block)
}