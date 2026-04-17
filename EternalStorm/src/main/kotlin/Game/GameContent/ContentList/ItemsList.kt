package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState
import kotlin.lazy

object ItemsList {
    val axe: String by lazy { AppState.addNamespace("axe") }
    val spear: String by lazy { AppState.addNamespace("spear") }
    val pickaxe: String by lazy { AppState.addNamespace("pickaxe") }
    val hummer: String by lazy { AppState.addNamespace("hummer") }
    val wooden_pickaxe: String by lazy { AppState.addNamespace("wooden_pickaxe") }
    val stone_pickaxe: String by lazy { AppState.addNamespace("stone_pickaxe") }
    val copper_pickaxe: String by lazy { AppState.addNamespace("copper_pickaxe") }
    val tin_pickaxe: String by lazy { AppState.addNamespace("tin_pickaxe") }
    val bronze_pickaxe: String by lazy { AppState.addNamespace("bronze_pickaxe") }
    val iron_pickaxe: String by lazy { AppState.addNamespace("iron_pickaxe") }
    val magical_pickaxe: String by lazy { AppState.addNamespace("magical_pickaxe") }
    val celestial_pickaxe: String by lazy { AppState.addNamespace("celestial_pickaxe") }
    val cosmic_pickaxe: String by lazy { AppState.addNamespace("cosmic_pickaxe") }
    val wind_pickaxe: String by lazy { AppState.addNamespace("wind_pickaxe") }
    val solar_pickaxe: String by lazy { AppState.addNamespace("solar_pickaxe") }

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
    val big_test_block: String by lazy { AppState.addNamespace("big_test_block") }
}