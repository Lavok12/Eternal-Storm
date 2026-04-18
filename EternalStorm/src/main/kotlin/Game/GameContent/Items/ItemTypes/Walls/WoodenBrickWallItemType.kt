package la.vok.Game.GameContent.Items.ItemTypes.Walls

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.State.AppState
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseWallItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class WoodenBrickWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.wooden_brick_wall
    override val texture: String = AppState.res("wooden_bricks.png")
    override val usingVariants = UsingVariants.PlaceWall(WallList.wooden_brick_wall)
}
