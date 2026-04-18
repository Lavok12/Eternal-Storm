package la.vok.Game.GameContent.Items.ItemTypes.Walls

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseWallItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class StoneBrickWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.stone_brick_wall
    override val texture: String = AppState.res("stone_bricks.png")
    override val usingVariants = UsingVariants.PlaceWall(WallList.stone_brick_wall)
}
