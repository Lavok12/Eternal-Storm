package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class StonePickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 12
    override val minePower = 10
    override val handItemUseDuration = 30f
    override val handItemTexture = "stone_pickaxe.png"
    override val handItemDamage = 3
}
