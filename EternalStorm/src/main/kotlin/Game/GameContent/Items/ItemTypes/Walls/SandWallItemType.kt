package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.State.AppState

class SandWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.sand_wall
    override val texture: String = AppState.res("sandTexture.jpg")
    override val usingVariants = UsingVariants.PlaceWall(WallList.sand_wall)
}
