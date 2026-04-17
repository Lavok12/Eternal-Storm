package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class CelestialPickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 80
    override val minePower = 100
    override val handItemUseDuration = 15f
    override val handItemTexture = "celestial_pickaxe.png"
    override val handItemDamage = 15
}
