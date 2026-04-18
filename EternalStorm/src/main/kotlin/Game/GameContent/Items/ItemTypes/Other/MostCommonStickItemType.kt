package la.vok.Game.GameContent.Items.ItemTypes.Other

import la.vok.Game.GameContent.Items.Items.MostCommonStickItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Items.Other.ItemRenderConfig
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class MostCommonStickItemType : AbstractItemType() {
    override val tag: String = ItemsList.most_common_stick
    override val texture = "most_common_stick.png"

    override val tags = setOf(ItemTags.WEAPON)

    override val maxInStack: Int = 1

    init {
        renderConfig.worldDelta = 0 v -0.23f
    }

    override fun createItem(gameCycle: GameCycle) : Item {
        return MostCommonStickItem(this, gameCycle)
    }
}