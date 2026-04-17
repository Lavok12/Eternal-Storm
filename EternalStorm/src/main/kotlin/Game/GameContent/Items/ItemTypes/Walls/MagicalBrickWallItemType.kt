package la.vok.Game.GameContent.Items.ItemTypes.Walls

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseWallItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class MagicalBrickWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.magical_brick_wall
    override val texture = "magical_bricks.png"
    override val usingVariants = UsingVariants.PlaceWall(WallList.magical_brick_wall)
}
