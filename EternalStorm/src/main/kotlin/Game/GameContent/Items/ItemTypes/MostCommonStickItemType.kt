package la.vok.Game.GameContent.Items.ItemTypes

import la.vok.Game.GameContent.Items.Items.AxeItem
import la.vok.Game.GameContent.Items.Items.MostCommonStickItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class MostCommonStickItemType : AbstractItemType() {
    override val tag: String = ItemsList.most_common_stick
    override val sprite = "most_common_stick.png"
    override val worldRenderDelta: Vec2 = 0 v -0.23f

    override val maxInStack: Int
        get() = 1

    override fun createItem(gameCycle: GameCycle) : Item {
        return MostCommonStickItem(this, gameCycle)
    }
}