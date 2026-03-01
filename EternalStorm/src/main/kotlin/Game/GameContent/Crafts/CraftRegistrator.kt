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
            ingredient(ItemsList.spear, 1)
        })

        reg.registrationCraft(craft {
            priority(1)
            result(ItemsList.axe, 1)
            ingredient(ItemsList.stone_block, 3)
            ingredient(ItemsList.spear, 1)
        })

        reg.registrationCraft(craft {
            priority(0)
            result(ItemsList.spear, 1)
            ingredient(ItemsList.stone_block, 2)
            ingredient(ItemsList.dirt_block, 1)
        })

        reg.registrationCraft(craft {
            priority(0)
            result(ItemsList.dirt_block, 4)
            ingredient(ItemsList.grass_block, 2)
            extra(ItemsList.stone_block, 1)
        })

        reg.registrationCraft(craft {
            priority(2)
            result(ItemsList.stone_block, 2)
            ingredient(ItemsList.dirt_block, 4)
        })

        reg.registrationCraft(craft {
            priority(0)
            result(ItemsList.grass_block, 1)
            ingredient(ItemsList.dirt_block, 2)
            ingredient(ItemsList.stone_block, 1)
        })

        reg.registrationCraft(craft {
            priority(3)
            result(ItemsList.axe, 1)
            ingredient(ItemsList.pickaxe, 1)
            ingredient(ItemsList.stone_block, 5)
            extra(ItemsList.spear, 1)
        })

        reg.registrationCraft(craft {
            priority(1)
            result(ItemsList.stone_block, 8)
            ingredient(ItemsList.stone_block, 6)
            ingredient(ItemsList.dirt_block, 2)
        })

        // --- Дорогие ---

        reg.registrationCraft(craft {
            priority(5)
            result(ItemsList.pickaxe, 3)
            ingredient(ItemsList.stone_block, 50)
            ingredient(ItemsList.dirt_block, 30)
            ingredient(ItemsList.axe, 2)
            extra(ItemsList.spear, 2)
        })

        reg.registrationCraft(craft {
            priority(5)
            result(ItemsList.axe, 5)
            ingredient(ItemsList.pickaxe, 3)
            ingredient(ItemsList.stone_block, 40)
            ingredient(ItemsList.grass_block, 20)
        })

        reg.registrationCraft(craft {
            priority(6)
            result(ItemsList.spear, 10)
            ingredient(ItemsList.stone_block, 100)
            ingredient(ItemsList.dirt_block, 50)
            ingredient(ItemsList.grass_block, 50)
        })

        // --- Ультра дорогие ---

        reg.registrationCraft(craft {
            priority(10)
            result(ItemsList.pickaxe, 1)
            ingredient(ItemsList.stone_block, 500)
            ingredient(ItemsList.dirt_block, 500)
            ingredient(ItemsList.grass_block, 500)
            ingredient(ItemsList.axe, 10)
            ingredient(ItemsList.spear, 10)
            extra(ItemsList.pickaxe, 1)
        })

        reg.registrationCraft(craft {
            priority(10)
            result(ItemsList.axe, 1)
            ingredient(ItemsList.stone_block, 999)
            ingredient(ItemsList.dirt_block, 999)
            ingredient(ItemsList.grass_block, 999)
            ingredient(ItemsList.pickaxe, 20)
            ingredient(ItemsList.spear, 20)
        })

        reg.registrationCraft(craft {
            priority(10)
            result(ItemsList.stone_block, 1)
            ingredient(ItemsList.axe, 50)
            ingredient(ItemsList.pickaxe, 50)
            ingredient(ItemsList.spear, 50)
            ingredient(ItemsList.grass_block, 1000)
            ingredient(ItemsList.dirt_block, 1000)
            extra(ItemsList.stone_block, 999)
        })

        reg.registrationCraft(craft {
            priority(9)
            result(ItemsList.grass_block, 100)
            ingredient(ItemsList.stone_block, 300)
            ingredient(ItemsList.dirt_block, 300)
            ingredient(ItemsList.axe, 5)
            ingredient(ItemsList.pickaxe, 5)
            ingredient(ItemsList.spear, 5)
        })

        reg.registrationCraft(craft {
            priority(9)
            result(ItemsList.spear, 50)
            ingredient(ItemsList.stone_block, 777)
            ingredient(ItemsList.grass_block, 333)
            ingredient(ItemsList.dirt_block, 444)
            ingredient(ItemsList.axe, 15)
            extra(ItemsList.pickaxe, 5)
            extra(ItemsList.axe, 5)
        })

        reg.registrationCraft(craft {
            priority(8)
            result(ItemsList.dirt_block, 200)
            ingredient(ItemsList.stone_block, 400)
            ingredient(ItemsList.grass_block, 200)
            ingredient(ItemsList.pickaxe, 8)
            ingredient(ItemsList.spear, 12)
        })
    }
}