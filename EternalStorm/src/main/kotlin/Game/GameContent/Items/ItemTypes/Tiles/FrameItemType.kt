package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.LavokLibrary.Vectors.Vec2

class FrameItemType : BaseTileItemType() {
    override val tag: String = ItemsList.frame
    override val texture: String = AppState.res("frame.png")

    override val renderConfig = super.renderConfig.copy(
        worldSize = Vec2(0.9f, 0.9f),
        sizeInSlot = Vec2(1.3f, 1.3f)
    )

    override val usingVariants = UsingVariants.PlaceTile(TilesList.frame_block)
}
