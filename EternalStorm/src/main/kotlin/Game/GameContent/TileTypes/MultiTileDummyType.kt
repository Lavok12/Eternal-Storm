package la.vok.Game.GameContent.Tiles.System

import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.Game.GameController.CollisionType

class MultiTileDummyType(
    override val tag: String,
    override val masterOffset: LPoint
) : AbstractTileType() {
    override val isDummy: Boolean = true
    
    // Most properties are inherited or not used for dummies
    override val maxHp: Int = 1
    override val texture: String = ""
    
    init {
        collisionType = CollisionType.FULL // Stay solid
    }
}
