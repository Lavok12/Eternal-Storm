package la.vok.Game.GameContent.Tiles.System

import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.State.AppState
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.Game.GameController.CollisionType

class MultiTileDummyType(
    override val tag: String,
    override val masterOffset: LPoint
) : AbstractTileType() {
    override val isDummy: Boolean = true

    override val maxHp: Int = 1
    override val texture: String = AppState.res("dummy.png")
    override val tags: Set<String> = setOf(BlockTags.NO_SHADOW)
    
    init {
        collisionType = CollisionType.NONE
    }
}
