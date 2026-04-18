package la.vok.Game.GameContent.Items.ItemTypes.Walls

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseWallItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class CopperBrickWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.copper_brick_wall
    override val texture: String = AppState.res("copper_bricks.png")
    override val usingVariants = UsingVariants.PlaceWall(WallList.copper_brick_wall)
}
