package la.vok.Game.Windows

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.Game.GameContent.Crafts.CraftType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class CraftCell(
    window: AbstractWindow,
    position: Vec2 = Vec2.ZERO,
    size: Vec2 = Vec2.ZERO,
    align: Vec2 = Vec2.ZERO,
    var craft: CraftType?,
    var completionPercent: Float = 0f,
    leftClick: WindowElement.(Vec2) -> Unit = {},
) : WindowElement(
    window = window,
    position = position,
    size = size,
    align = align,
    insideMethod = EMPTY_INSIDE,
    preRender = {},
    render = {},
    postRender = {},
    resize = {},
    update = {},
    physicUpdate = {},
    leftClick = leftClick,
    rightClick = {},
    start = {},
    tooltip = null
) {
    override fun callPreRender(lg: LGraphics) {
        if (!isVisible) return
        lg.fill(0f, 20f)
        for (i in 1..4) lg.setBlock(positionWithCache, size * 0.9f * (1f + i / 30f))
    }

    override fun callRender(lg: LGraphics) {
        if (!isVisible) return

        // Фон по проценту доступности
        val alpha = 80f + completionPercent * 80f
        when {
            completionPercent >= 1f -> lg.fill(140f, 210f, 140f, alpha)
            completionPercent > 0f  -> lg.fill(210f, 180f, 100f, alpha)
            else                    -> lg.fill(100f, 100f, 110f, alpha)
        }
        lg.noStroke()
        lg.setBlock(positionWithCache, size * 0.9f)

        if (completionPercent < 1) {
            // Полоска прогресса снизу
            val barH = size.y * 0.06f
            val barW = size.x * 0.9f * completionPercent
            lg.fill(255f, 255f, 100f, 160f)
            lg.setBlock(
                positionWithCache + ((-(size.x * 0.9f - barW) / 2f) v (-size.y * 0.9f / 2f + barH / 2f)),
                barW v barH
            )
        }

        // Иконка результата
        val resultType = craft?.resultType ?: return
        lg.setImage(window.coreController.spriteLoader.getValue(resultType.texture), positionWithCache, size * 0.75f)

        // Количество результата
        val count = craft?.result?.count ?: return
        if (count > 1) {
            lg.fill(255f)
            lg.setText("×$count", positionWithCache + (size.x * 0.2f v  -size.y * 0.44f), size.y * 0.3f)
        }
    }

    override fun callPostRender(lg: LGraphics) {}
    override fun callResize() {}
}