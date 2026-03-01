package la.vok.Game.GameContent.Crafts

import Core.CoreControllers.Loaders.MainContentRegistration
import Core.CoreControllers.ObjectRegistration
import la.vok.Game.GameContent.ItemsList

object CraftRegistrator {
    fun registration(reg: ObjectRegistration) {

        // --- Базовые ---

        reg.registrationCraft(craft {
            priority(1)
            result(ItemsList.pickaxe, 1)
            ingredient(ItemsList.stone_block, 3)
        })
        reg.registrationCraft(craft {
            priority(1)
            result(ItemsList.stone_block, 1)
            ingredient(ItemsList.grass_block, 3)
        })
        reg.registrationCraft(craft {
            priority(1)
            result(ItemsList.spear, 1)
            ingredient(ItemsList.dirt_block, 3)
        })
    }
}