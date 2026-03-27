package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState

object WallList {

    val dirt_wall: String by lazy { AppState.addNamespace("dirt_wall") }
    val plank_wall: String by lazy { AppState.addNamespace("plank_wall") }

}