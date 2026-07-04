package la.vok.Game.GameContent.LiquidTypes

import la.vok.Game.GameContent.ContentList.LiquidList
import la.vok.LavokLibrary.Vectors.LColor

import la.vok.Game.GameContent.CustomBuffTypes.BuffType
import la.vok.Game.GameSystems.EntityComponents.Buffs.BuffRegistry
import la.vok.Game.GameContent.ContentList.BuffTags

class AcidLiquidType : AbstractLiquidType() {
    override val id: Byte = LiquidList.ACID_ID
    override val tag: String = LiquidList.acid
    override val color: LColor = LColor(100f, 255f, 50f, 180f)
    override val viscosity: Int = 2
    override val density: Int = 15
    override val viscosityForEntities: Float = 0.25f
    override val touchBuff: BuffType? get() = BuffRegistry.get(BuffTags.ACID_BURN)
}
