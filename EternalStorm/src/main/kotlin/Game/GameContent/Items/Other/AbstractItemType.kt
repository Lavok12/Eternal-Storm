package la.vok.Game.GameContent.Items.Other

import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

data class ItemRenderConfig(
    var worldSize: Vec2 = 1f v 1f,
    var sizeInSlot: Vec2 = 1f v 1f,
    var worldDelta: Vec2 = 0f v 0f,
    var slotDelta: Vec2 = 0f v 0f,
    var shadowPower: Float = 1.0f,
    var sizeMultiplier: Float = 1.0f
)

abstract class AbstractItemType {
    open val tag: String = ""
    open val renderConfig: ItemRenderConfig = ItemRenderConfig()

    open val texture = ""
    open val usingVariants: UsingVariants = UsingVariants.Custom
    open val maxInStack = 1
    open val tags: Set<String> = emptySet()

    fun hasTag(tag: String): Boolean = tag in tags

    open fun createItem(gameCycle: GameCycle) : Item {
        return Item(this, gameCycle)
    }
}