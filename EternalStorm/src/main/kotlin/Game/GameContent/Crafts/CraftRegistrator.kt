package la.vok.Game.GameContent.Crafts

import Core.CoreControllers.Loaders.MainContentRegistration
import Core.CoreControllers.ObjectRegistration
import la.vok.Game.GameContent.ItemsList

object CraftRegistrator {
    fun registration(reg: ObjectRegistration) {

        // --- Базовые ---

        reg.registrationCraft(craft {
            priority(1)
            result(ItemsList.most_common_stick, 1)
            ingredient(ItemsList.stone_block, 3)
        })
    }
}