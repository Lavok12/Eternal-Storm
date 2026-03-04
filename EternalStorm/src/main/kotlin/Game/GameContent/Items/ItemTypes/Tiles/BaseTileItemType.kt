package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Items.UniversalBlockItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class BaseTileItemType : AbstractItemType() {
    override val tag: String = ItemsList.dirt_block
    override val texture = "dirtTexture.jpg"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.dirt_block)
    override val maxInStack: Int = 99999
    override val worldSize: Vec2 = 0.5f v 0.5
    override val sizeInSlot: Vec2 = 0.8f v 0.8f
    override val worldRenderDelta: Vec2 = 0 v (-0)
    override val shadowPower: Float = 0.35f

    override fun createItem(gameCycle: GameCycle): Item {
        return UniversalBlockItem(this, gameCycle)
    }
}