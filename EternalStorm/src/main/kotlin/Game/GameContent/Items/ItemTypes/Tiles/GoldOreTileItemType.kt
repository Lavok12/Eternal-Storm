package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class GoldOreTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.gold_ore_block
    override val texture: String = AppState.res("gold_ore_texture.jpg")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.gold_ore_block)
}
