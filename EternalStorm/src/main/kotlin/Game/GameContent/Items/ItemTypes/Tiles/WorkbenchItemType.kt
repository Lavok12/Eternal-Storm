package la.vok.Game.GameContent.Items.ItemTypes.Tiles

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.BaseTileItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.LavokLibrary.Vectors.Vec2

class WorkbenchItemType : BaseTileItemType() {
    override val tag: String = ItemsList.workbench
    override val texture: String = AppState.res("workbench.png")

    override val renderConfig = super.renderConfig.copy(
        worldSize = Vec2(1.32f, 1.32f),
        sizeInSlot = Vec2(1.6f, 1.6f),
        worldDelta = Vec2(0f, 0.08f)
    )

    override val usingVariants = UsingVariants.PlaceTile(TilesList.workbench)
}
