package la.vok.Game.GameContent.Items.ItemTypes.Walls

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseWallItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants

class PlankWallItemType : BaseWallItemType() {
    override val tag: String = ItemsList.plank_wall
    override val texture = "plankTexture.jpg"
    override val usingVariants = UsingVariants.PlaceWall(WallList.plank_wall)
    override val tags = super.tags
}