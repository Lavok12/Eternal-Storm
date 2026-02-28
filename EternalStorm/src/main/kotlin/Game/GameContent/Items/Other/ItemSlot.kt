package la.vok.Game.GameContent.Items.Other

import la.vok.Game.GameController.GameCycle

class ItemSlot(var gameCycle: GameCycle, var itemContainer: ItemContainer, val id: Int = 0) {
    var item: Item? = null
    var isBlocked: Boolean = false

    fun isFree(): Boolean {
        if (isBlocked) return false
        val item = item ?: return true
        return item.count <= 0
    }

    fun isActive(): Boolean {
        return !isBlocked
    }
}