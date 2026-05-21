package la.vok.Game.GameContent.LiquidTypes

import la.vok.Game.GameContent.ContentList.LiquidList
import la.vok.LavokLibrary.Vectors.LColor

class AcidLiquidType : AbstractLiquidType() {
    override val id: Byte = LiquidList.ACID_ID
    override val tag: String = LiquidList.acid
    override val color: LColor = LColor(100f, 255f, 50f, 180f)
    override val viscosity: Int = 2
    override val density: Int = 15
}
