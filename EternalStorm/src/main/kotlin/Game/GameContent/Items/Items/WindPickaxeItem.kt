package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle

class WindPickaxeItem(itemType: AbstractItemType, gameCycle: GameCycle) : PickaxeItem(itemType, gameCycle) {
    override val mineDamage = 60
    override val minePower = 80
    override val handItemUseDuration = 8f
    override val handItemTexture = "wind_pickaxe.png"
    override val handItemDamage = 12
}
