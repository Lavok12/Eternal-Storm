package la.vok.Game.GameContent

import la.vok.State.AppState

object EntitiesList {
    val player: String by lazy { AppState.addNamespace("Player") }
    val slime: String by lazy { AppState.addNamespace("Slime") }
    val projectile: String by lazy { AppState.addNamespace("Projectile") }
    val boss: String by lazy { AppState.addNamespace("Boss") }
}