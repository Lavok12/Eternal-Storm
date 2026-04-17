package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class BronzePickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 20
    override val minePower = 25
    override val handItemUseDuration = 22f
    override val handItemTexture = "bronze_pickaxe.png"
    override val handItemDamage = 5
}
