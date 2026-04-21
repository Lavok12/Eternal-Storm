package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Items.Other.ItemRenderConfig
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class SunflowerItemType : BaseTileItemType() {
    override val tag: String = ItemsList.sunflower
    override val texture: String = AppState.res("sunflower.png")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.sunflower)
    override val renderConfig: ItemRenderConfig = ItemRenderConfig(worldSize = 1.2f v 1.2f, sizeInSlot = 1.5f v 1.5f)
}
