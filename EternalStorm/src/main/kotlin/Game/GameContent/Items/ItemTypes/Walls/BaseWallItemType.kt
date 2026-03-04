package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Items.UniversalBlockItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.Items.Items.UniversalWallItem
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class BaseWallItemType : AbstractItemType() {
    override val tag: String = ItemsList.dirt_block
    override val texture = "dirtTexture.jpg"
    override val usingVariants = UsingVariants.PlaceWall(WallList.dirt_wall)
    override val maxInStack: Int = 99999
    override val worldSize: Vec2 = 0.7f v 0.7
    override val sizeInSlot: Vec2 = 0.9f v 0.9f
    override val worldRenderDelta: Vec2 = 0 v -0.12f
    override val shadowPower: Float = 0.35f

    override fun createItem(gameCycle: GameCycle): Item {
        return UniversalWallItem(this, gameCycle)
    }
}