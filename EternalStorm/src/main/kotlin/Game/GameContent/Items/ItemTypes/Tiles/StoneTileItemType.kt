package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList

class StoneTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.stone_block
    override val texture = "stone_texture.jpg"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.stone_block)
}