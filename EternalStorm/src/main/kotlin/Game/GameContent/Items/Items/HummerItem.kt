package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.List.HummerHandItem
import la.vok.Game.GameContent.HandItems.Weapons.PickaxeHandItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
open class HummerItem(itemType: AbstractItemType, gameCycle: GameCycle) : Item(itemType, gameCycle) {
    open val mineDamage = 300
    open val minePower = 100
    open val handItemUseDuration = 8f
    open val handItemTexture = "hummer.png"
    open val handItemSize = 3f
    open val handItemDamage = 4
    open val handItemKnockback = 0.4 v 0.3

    override fun getHandItem(component: HandItemComponent): HandItem? {
        return HummerHandItem(this, component)
    }
}