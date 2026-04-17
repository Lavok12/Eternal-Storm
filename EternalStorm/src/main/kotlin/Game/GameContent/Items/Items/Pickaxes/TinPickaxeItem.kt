package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class TinPickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 15
    override val minePower = 15
    override val handItemUseDuration = 25f
    override val handItemTexture = "tin_pickaxe.png"
    override val handItemDamage = 4
}
