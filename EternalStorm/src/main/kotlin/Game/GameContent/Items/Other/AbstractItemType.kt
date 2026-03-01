package la.vok.Game.GameContent.Items.Other

import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

abstract class AbstractItemType {
    open val tag: String = ""
    open val worldSize: Vec2 = 1 v 1
    open val sizeInSlot: Vec2 = 1 v 1
    open val worldRenderDelta: Vec2 = 0 v 0
    open val slotRenderDelta: Vec2 = 0 v 0
    open val shadowPower = 0.7f

    open val sprite = ""
    open val usingVariants: UsingVariants = UsingVariants.Custom
    open val maxInStack = 1

    open fun createItem(gameCycle: GameCycle) : Item {
        return Item(this, gameCycle)
    }
}