package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState
import kotlin.lazy

object ItemsList {
    val axe: String by lazy { AppState.addNamespace("axe") }
    val spear: String by lazy { AppState.addNamespace("spear") }
    val pickaxe: String by lazy { AppState.addNamespace("pickaxe") }
    val hummer: String by lazy { AppState.addNamespace("hummer") }

    val grass_block: String by lazy { AppState.addNamespace("grass_block") }
    val dirt_block: String by lazy { AppState.addNamespace("dirt_block") }
    val dirt_wall: String by lazy { AppState.addNamespace("dirt_wall") }
    val plank_block: String by lazy { AppState.addNamespace("plank_block") }
    val plank_wall: String by lazy { AppState.addNamespace("plank_wall") }
    val stone_block: String by lazy { AppState.addNamespace("stone_block") }
    val gold_ore_block: String by lazy { AppState.addNamespace("gold_ore_block") }
    val diamond_ore_block: String by lazy { AppState.addNamespace("diamond_ore_block") }
    val most_common_stick: String by lazy { AppState.addNamespace("most_common_stick") }
    val teleporter: String by lazy { AppState.addNamespace("teleporter") }
}