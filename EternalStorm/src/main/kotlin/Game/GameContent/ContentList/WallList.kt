package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState

object WallList {

    val dirt_wall: String by lazy { AppState.addNamespace("dirt_wall") }
    val plank_wall: String by lazy { AppState.addNamespace("plank_wall") }

    val wooden_brick_wall: String by lazy { AppState.addNamespace("wooden_brick_wall") }
    val stone_brick_wall: String by lazy { AppState.addNamespace("stone_brick_wall") }
    val copper_brick_wall: String by lazy { AppState.addNamespace("copper_brick_wall") }
    val tin_brick_wall: String by lazy { AppState.addNamespace("tin_brick_wall") }
    val bronze_brick_wall: String by lazy { AppState.addNamespace("bronze_brick_wall") }
    val iron_brick_wall: String by lazy { AppState.addNamespace("iron_brick_wall") }
    val magical_brick_wall: String by lazy { AppState.addNamespace("magical_brick_wall") }
    val celestial_brick_wall: String by lazy { AppState.addNamespace("celestial_brick_wall") }
    val cosmic_brick_wall: String by lazy { AppState.addNamespace("cosmic_brick_wall") }
    val wind_brick_wall: String by lazy { AppState.addNamespace("wind_brick_wall") }
    val solar_brick_wall: String by lazy { AppState.addNamespace("solar_brick_wall") }

}