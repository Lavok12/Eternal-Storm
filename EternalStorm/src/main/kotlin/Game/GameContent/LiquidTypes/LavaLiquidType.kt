package la.vok.Game.GameContent.LiquidTypes

import la.vok.Game.GameContent.ContentList.LiquidList
import la.vok.LavokLibrary.Vectors.LColor

class LavaLiquidType : AbstractLiquidType() {
    override val id: Byte = LiquidList.LAVA_ID
    override val tag: String = LiquidList.lava
    override val color: LColor = LColor(255f, 80f, 0f, 240f)
    override val viscosity: Int = 4
    override val density: Int = 20
}
