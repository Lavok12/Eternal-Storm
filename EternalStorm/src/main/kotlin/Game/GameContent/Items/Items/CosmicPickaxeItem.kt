package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class CosmicPickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 120
    override val minePower = 150
    override val handItemUseDuration = 12f
    override val handItemTexture = "cosmic_pickaxe.png"
    override val handItemDamage = 20
}
