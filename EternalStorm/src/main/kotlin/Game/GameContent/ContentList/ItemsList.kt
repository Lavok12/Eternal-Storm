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
    val stone_block: String by lazy { AppState.addNamespace("stone_block") }
    val most_common_stick: String by lazy { AppState.addNamespace("most_common_stick") }
}