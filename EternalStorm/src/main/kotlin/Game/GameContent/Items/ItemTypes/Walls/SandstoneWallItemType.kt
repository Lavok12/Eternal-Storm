package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.State.AppState

class SandstoneWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.sandstone_wall
    override val texture: String = AppState.res("sandstoneTexture.jpg")
    override val usingVariants = UsingVariants.PlaceWall(WallList.sandstone_wall)
}
