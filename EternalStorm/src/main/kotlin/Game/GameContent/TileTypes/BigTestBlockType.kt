package la.vok.Game.GameContent.Tiles.System

import la.vok.LavokLibrary.Vectors.*
import la.vok.State.AppState

class BigTestBlockType : AbstractTileType() {
    override val tag: String = la.vok.Game.GameContent.ContentList.TilesList.big_test_block
    override val texture: String = AppState.res("big_test_block.png")
    override val maxHp: Int = 100
    override val blockStrength: Int = 1
    
    override val width: Int = 3 
    override val height: Int = 3
    override val placeOffset: LPoint = -1 p -1

    override val renderConfig = TileRenderConfig()
}
