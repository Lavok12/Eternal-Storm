package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class WoodenPickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 8
    override val minePower = 5
    override val handItemUseDuration = 35f
    override val handItemTexture = "wooden_pickaxe.png"
    override val handItemDamage = 2
}
