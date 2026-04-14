package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class PlankTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.plank_block
    override val texture = "plankTexture.jpg"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.plank_block)
    override val tags = super.tags + ItemTags.PLANK
}