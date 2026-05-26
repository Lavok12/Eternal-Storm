package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState

object EntitiesList {
    val player: String by lazy { AppState.tag("Player") }
    val slime: String by lazy { AppState.tag("Slime") }
    val projectile: String by lazy { AppState.tag("Projectile") }
    val boss: String by lazy { AppState.tag("Boss") }
    val item: String by lazy { AppState.tag("Item") }
    val empty: String by lazy { AppState.tag("Empty") }
    val fallingBlock: String by lazy { AppState.tag("FallingBlock") }
    val damage: String by lazy { AppState.tag("Damage") }
    val spider_boss: String by lazy { AppState.tag("SpiderBoss") }
    val spider_leg: String by lazy { AppState.tag("SpiderLeg") }
}