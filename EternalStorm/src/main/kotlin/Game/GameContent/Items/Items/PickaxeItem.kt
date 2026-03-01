package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.Items.AxeHandItem
import la.vok.Game.GameContent.HandItems.Weapons.PickaxeHandItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.LavokLibrary.Vectors.v

@Suppress("UNCHECKED_CAST")
open class PickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : Item(itemType, gameCycle) {
    open val mineDamage = 40
    open val minePower = 100
    open val handItemUseDuration = 5f
    open val handItemTexture = "pickaxe.png"
    open val handItemSize = 3f
    open val handItemDamage = 4
    open val handItemKnockback = 0.2 v 0.15

    override fun getHandItem(component: HandItemComponent): HandItem? {
        return PickaxeHandItem(this, component)
    }
}