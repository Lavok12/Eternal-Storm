package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList

class DiamondOreTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.diamond_ore_block
    override val texture = "diamond_ore_texture.jpg"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.diamond_ore_block)
}
