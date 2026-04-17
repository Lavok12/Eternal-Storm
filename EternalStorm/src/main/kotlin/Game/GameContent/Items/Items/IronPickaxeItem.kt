package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class IronPickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 30
    override val minePower = 40
    override val handItemUseDuration = 20f
    override val handItemTexture = "iron_pickaxe.png"
    override val handItemDamage = 7
}
