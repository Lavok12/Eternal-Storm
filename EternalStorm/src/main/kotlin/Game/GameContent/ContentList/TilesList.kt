package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState

object TilesList {
    val stone_block: String by lazy { AppState.addNamespace("stone_block") }
    val grass_block: String by lazy { AppState.addNamespace("grass_block") }
    val dirt_block: String by lazy { AppState.addNamespace("dirt_block") }
    val plank_block: String by lazy { AppState.addNamespace("plank_block") }
    val tree_part_block: String by lazy { AppState.addNamespace("tree_part_block") }
    val gold_ore_block: String by lazy { AppState.addNamespace("gold_ore_block") }
    val diamond_ore_block: String by lazy { AppState.addNamespace("diamond_ore_block") }
}