package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.State.AppState

class DirtWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.dirt_wall
    override val texture: String = AppState.res("dirtTexture.jpg")
    override val usingVariants = UsingVariants.PlaceWall(WallList.dirt_wall)
}