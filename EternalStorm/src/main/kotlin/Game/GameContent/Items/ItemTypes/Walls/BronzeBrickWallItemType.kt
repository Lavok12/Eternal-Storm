package la.vok.Game.GameContent.Items.ItemTypes.Walls

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseWallItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class BronzeBrickWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.bronze_brick_wall
    override val texture: String = AppState.res("bronze_bricks.png")
    override val usingVariants = UsingVariants.PlaceWall(WallList.bronze_brick_wall)
}
