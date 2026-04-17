package la.vok.Game.GameContent.Items.ItemTypes.Walls

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseWallItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class TinBrickWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.tin_brick_wall
    override val texture = "tin_bricks.png"
    override val usingVariants = UsingVariants.PlaceWall(WallList.tin_brick_wall)
}
