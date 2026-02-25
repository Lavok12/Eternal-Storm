package la.vok.Game.GameContent

import la.vok.State.AppState

object TilesList {
    val stone: String by lazy { AppState.addNamespace("stone") }
    val grass: String by lazy { AppState.addNamespace("grass") }
    val dirt: String by lazy { AppState.addNamespace("dirt") }
}