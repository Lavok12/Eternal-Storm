package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class CopperOreTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.copper_ore_block
    override val texture: String = AppState.res("copper_ore_texture.png")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.copper_ore_block)
}
