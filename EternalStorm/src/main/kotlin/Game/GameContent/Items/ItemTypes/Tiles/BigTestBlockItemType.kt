package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Items.Other.UsingVariants

class BigTestBlockItemType : BaseTileItemType() {
    override val tag: String = ItemsList.big_test_block
    override val texture = "3x3.png"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.big_test_block)
}
