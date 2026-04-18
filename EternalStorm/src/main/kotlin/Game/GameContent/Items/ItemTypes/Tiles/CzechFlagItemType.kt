package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v

class CzechFlagItemType : BaseTileItemType() {
    override val tag: String = ItemsList.czech_flag
    override val texture: String = "czech_flag.png"

    override val renderConfig = super.renderConfig.copy(
        worldSize = 1.35f v 1.35f,
        sizeInSlot = 1.24f v 1.24f,
        worldDelta = 0 v 0.13f
    )
    
    override val usingVariants = UsingVariants.PlaceTile(TilesList.czech_flag)
}
