package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState

object DimensionsList {
    val main: String by lazy { AppState.addNamespace("Main") }
    val stone_world: String by lazy { AppState.addNamespace("StoneWorld") }
    val brick_world: String by lazy { AppState.addNamespace("BrickWorld") }
}