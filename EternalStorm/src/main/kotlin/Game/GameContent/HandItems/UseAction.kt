package la.vok.Game.GameContent.HandItems

import la.vok.Game.GameContent.Items.Other.Item
import la.vok.LavokLibrary.Vectors.Vec2

sealed class UseAction {

    object None : UseAction()

    data class Custom(
        val onStart: (HandItem.(target: Vec2) -> Unit)? = null,
        val onProgress: (HandItem.(progress: Float) -> Unit)? = null,
        val onEnd: (HandItem.() -> Unit)? = null,
        val triggerAt: Float? = null
    ) : UseAction()
}