package la.vok.Game.GameContent.Items.Items.Hoes

import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.List.HoeHandItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
open class HoeItem(itemType: AbstractItemType, gameCycle: GameCycle) : Item(itemType, gameCycle) {
    open val handItemUseDuration = 20f
    open val handItemTexture = "copper_hoe.png"
    open val handItemSize = 3f
    open val handItemDamage = 4
    open val handItemKnockback = 0.2 v 0.15

    override fun getHandItem(component: HandItemComponent): HandItem? {
        return HoeHandItem(this, component)
    }
}
