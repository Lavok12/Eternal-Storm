package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState

object EntitiesList {
    val player: String by lazy { AppState.tag("Player") }
    val slime: String by lazy { AppState.tag("Slime") }
    val projectile: String by lazy { AppState.tag("Projectile") }
    val boss: String by lazy { AppState.tag("Boss") }
}