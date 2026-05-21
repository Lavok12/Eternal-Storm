package la.vok.Game.GameContent.LiquidTypes

import la.vok.Game.GameContent.ContentList.LiquidList
import la.vok.LavokLibrary.Vectors.LColor

class WaterLiquidType : AbstractLiquidType() {
    override val id: Byte = LiquidList.WATER_ID
    override val tag: String = LiquidList.water
    override val color: LColor = LColor(50f, 100f, 255f, 180f)
    override val viscosity: Int = 1
    override val density: Int = 10
}
