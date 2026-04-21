package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState

class SmallGrassTileItemType : BaseTileItemType() {
    override val tag: String = ItemsList.small_grass
    override val texture: String = AppState.res("small_grass.png")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.small_grass)
}
