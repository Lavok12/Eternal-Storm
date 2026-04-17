package la.vok.Game.GameContent.Items.Items

import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.v

class MaterialPickaxeItem(
    itemType: AbstractItemType,
    gameCycle: GameCycle,
    override val mineDamage: Int,
    override val minePower: Int,
    override val handItemUseDuration: Float,
    override val handItemTexture: String,
    override val handItemDamage: Int
) : PickaxeItem(itemType, gameCycle) {
    override val handItemKnockback = 0.2 v 0.15
    override val handItemSize = 3f
}
