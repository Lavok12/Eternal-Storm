package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.Game.GameController.CollisionType
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v

class CzechFlagTileType : AbstractTileType() {
    override val tag: String = TilesList.czech_flag
    override val texture: String = AppState.res("czech_flag.png")
    override val width: Int = 2
    override val height: Int = 3
    override var collisionType = CollisionType.NONE
    override val blockStrength = 10
    override val maxHp = 40
    override val tags: Set<String> = setOf(BlockTags.NO_SHADOW)
    override val drop = SingleDrop(ItemsList.czech_flag)
    
    override val renderConfig = TileRenderConfig(useSquareRender = true, sizeMultiplier = 1.7f, renderDelta = 0.55f v 0f)
    
    init {
        collisionType = CollisionType.NONE
    }
}
