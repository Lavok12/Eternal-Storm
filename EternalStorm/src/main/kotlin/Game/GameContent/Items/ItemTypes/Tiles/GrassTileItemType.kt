package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList

class GrassTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.grass_block
    override val texture = "grassTexture.jpg"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.grass_block)
}