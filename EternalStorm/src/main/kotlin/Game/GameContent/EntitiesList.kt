package la.vok.Game.GameContent

import la.vok.State.AppState

object EntitiesList {
    val player: String by lazy { AppState.addNamespace("Player") }
    val slime: String by lazy { AppState.addNamespace("Slime") }
}