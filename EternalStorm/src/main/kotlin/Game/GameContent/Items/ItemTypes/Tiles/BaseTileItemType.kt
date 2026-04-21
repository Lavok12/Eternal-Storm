package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Items.UniversalTileItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.Other.ItemRenderConfig
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

open class BaseTileItemType : AbstractItemType() {
    override val tag: String = ItemsList.dirt_block
    override val texture: String = AppState.res("dirtTexture.jpg")
    override val usingVariants = UsingVariants.PlaceTile(TilesList.dirt_block)
    override val maxInStack: Int = 99999
    override val tags = setOf(ItemTags.PLACEABLE)

    override val renderConfig = ItemRenderConfig(
        worldSize = 0.35f v 0.35f,
        sizeInSlot = 0.72f v 0.72f,
        worldDelta = 0 v (-0.08111f),
        shadowPower = 0.35f
    )

    override fun createItem(gameCycle: GameCycle): Item {
        return UniversalTileItem(this, gameCycle)
    }
}