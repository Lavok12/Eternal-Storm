package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class DiamondOreTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.diamond_ore_block
    override val texture: String = AppState.res("diamond_ore_texture.jpg")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.diamond_ore_block)
}
