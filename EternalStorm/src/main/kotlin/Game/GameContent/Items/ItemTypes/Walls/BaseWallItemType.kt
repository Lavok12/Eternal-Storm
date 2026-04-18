package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Items.Items.UniversalWallItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.ItemRenderConfig
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class BaseWallItemType : AbstractItemType() {
    override val tag: String = ItemsList.dirt_block
    override val texture = "dirtTexture.jpg"
    override val usingVariants = UsingVariants.PlaceWall(WallList.dirt_wall)
    override val maxInStack: Int = 99999
    override val tags = setOf(ItemTags.PLACEABLE)

    override val renderConfig = ItemRenderConfig(
        worldSize = 0.40f v 0.40f,
        sizeInSlot = 0.85f v 0.85f,
        worldDelta = 0 v -0.06f,
        shadowPower = 0.35f
    )

    override fun createItem(gameCycle: GameCycle): Item {
        return UniversalWallItem(this, gameCycle)
    }
}