package la.vok.Game.GameContent.Items.Other

import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

abstract class AbstractItemType {
    open val tag: String = ""
    open val worldSize: Vec2 = 1 v 1
    open val sizeInSlot: Vec2 = 1 v 1
    open val worldRenderDelta: Vec2 =  0 v 0f
    open val slotRenderDelta: Vec2 = Vec2.ZERO
    open val shadowPower: Float = 1.0f

    open val texture = ""
    open val usingVariants: UsingVariants = UsingVariants.Custom
    open val maxInStack = 1
    open val tags: Set<String> = emptySet()

    fun hasTag(tag: String): Boolean = tag in tags

    open fun createItem(gameCycle: GameCycle) : Item {
        return Item(this, gameCycle)
    }
}