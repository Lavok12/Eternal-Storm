package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Items.DirtItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameContent.TilesList
import la.vok.Game.GameController.GameCycle

class DirtItemType : AbstractItemType() {
    override val tag: String = ItemsList.dirt_block
    override val sprite = "dirtTexture.jpg"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.dirt_block)
    override val maxInStack: Int = 99999

    override fun createItem(gameCycle: GameCycle) : Item {
        return DirtItem(this, gameCycle)
    }
}