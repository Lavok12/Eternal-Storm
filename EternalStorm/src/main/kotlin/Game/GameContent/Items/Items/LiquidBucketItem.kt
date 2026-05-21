package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.List.LiquidHandItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent

class LiquidBucketItem(itemType: AbstractItemType, gameCycle: GameCycle, val liquidTag: String) : Item(itemType, gameCycle) {
    override fun getHandItem(component: HandItemComponent): HandItem? {
        return LiquidHandItem(this, component, liquidTag)
    }
}

abstract class LiquidBucketItemType(val liquidTag: String) : AbstractItemType() {
    override val texture: String = "texture_64x64 (2).png"
    override val maxInStack: Int = 1

    override fun createItem(gameCycle: GameCycle): Item {
        return LiquidBucketItem(this, gameCycle, liquidTag)
    }
}
