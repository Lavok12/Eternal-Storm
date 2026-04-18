package la.vok.Game.GameContent.Items.ItemTypes.Walls

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseWallItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class IronBrickWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.iron_brick_wall
    override val texture: String = AppState.res("iron_bricks.png")
    override val usingVariants = UsingVariants.PlaceWall(WallList.iron_brick_wall)
}
