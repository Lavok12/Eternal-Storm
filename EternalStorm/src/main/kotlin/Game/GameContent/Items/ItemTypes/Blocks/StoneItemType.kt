package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Items.StoneItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameContent.TilesList
import la.vok.Game.GameController.GameCycle

class StoneItemType : AbstractItemType() {
    override val tag: String = ItemsList.stone_block
    override val sprite = "stoneTexture.jpg"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.stone_block)
    override val maxInStack: Int = 99999

    override fun createItem(gameCycle: GameCycle) : Item {
        return StoneItem(this, gameCycle)
    }
}