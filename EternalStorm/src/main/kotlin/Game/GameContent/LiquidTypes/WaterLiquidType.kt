package la.vok.Game.GameContent.LiquidTypes

import la.vok.Game.GameContent.ContentList.LiquidList
import la.vok.LavokLibrary.Vectors.LColor

import la.vok.Game.GameContent.CustomBuffTypes.BuffType
import la.vok.Game.GameSystems.EntityComponents.Buffs.BuffRegistry
import la.vok.Game.GameContent.ContentList.BuffTags

class WaterLiquidType : AbstractLiquidType() {
    override val id: Byte = LiquidList.WATER_ID
    override val tag: String = LiquidList.water
    override val color: LColor = LColor(50f, 100f, 255f, 180f)
    override val viscosity: Int = 1
    override val density: Int = 10
    override val viscosityForEntities: Float = 0.15f
    override val touchBuff: BuffType? get() = BuffRegistry.get(BuffTags.WET)
}
