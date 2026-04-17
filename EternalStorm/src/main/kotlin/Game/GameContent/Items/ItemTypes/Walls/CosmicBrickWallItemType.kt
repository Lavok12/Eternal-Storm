package la.vok.Game.GameContent.Items.ItemTypes.Walls

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseWallItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class CosmicBrickWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.cosmic_brick_wall
    override val texture = "cosmic_bricks.png"
    override val usingVariants = UsingVariants.PlaceWall(WallList.cosmic_brick_wall)
}
