package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class SolarPickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 150
    override val minePower = 200
    override val handItemUseDuration = 14f
    override val handItemTexture = "solar_pickaxe.png"
    override val handItemDamage = 30
}
