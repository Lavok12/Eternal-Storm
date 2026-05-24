package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState

object LiquidList {
    val water: String by lazy { AppState.tag("water") }
    val lava: String by lazy { AppState.tag("lava") }
    val acid: String by lazy { AppState.tag("acid") }
    
    // ID Constants for internal use
    const val WATER_ID: Byte = 1
    const val LAVA_ID: Byte = 2
    const val ACID_ID: Byte = 3
}
