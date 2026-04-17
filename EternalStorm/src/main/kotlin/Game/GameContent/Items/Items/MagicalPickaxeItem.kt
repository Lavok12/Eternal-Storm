package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class MagicalPickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 50
    override val minePower = 60
    override val handItemUseDuration = 18f
    override val handItemTexture = "magical_pickaxe.png"
    override val handItemDamage = 10
}
