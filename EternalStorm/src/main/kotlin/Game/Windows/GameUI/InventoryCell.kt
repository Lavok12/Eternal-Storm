package la.vok.Game.Windows

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.Game.GameContent.Items.Other.ItemSlot
import la.vok.LavokLibrary.Gradient.ShadowFrameInfo
import la.vok.LavokLibrary.Gradient.ShadowType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.Vec2

class InventoryCell(
    window: AbstractWindow,
    position: Vec2 = Vec2.ZERO,
    size: Vec2 = Vec2.ZERO,
    align: Vec2 = Vec2.ZERO,
    var cellType: InventoryCellType = InventoryCellType.INVENTORY,
    var slot: ItemSlot?,
    update: WindowElement.() -> Unit = {},
    physicUpdate: WindowElement.() -> Unit = {},
    leftClick: WindowElement.(Vec2) -> Unit = {},
    rightClick: WindowElement.(Vec2) -> Unit = {},
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
    update = update,
    physicUpdate = physicUpdate,
    leftClick = leftClick,
    rightClick = rightClick,
    start = {},
    tooltip = null
) {

    var isDragTarget = false

    override fun callRender(lg: LGraphics) {
        if (!isVisible) return

        val isSelected = slot?.item?.isChoose == true
        
        // 1. Градиентный фон ячейки
        val bgTop = if (isSelected) LColor(75f, 83f, 95f, 220f)*1.4f*0.9f else LColor(68f, 71f, 83f, 180f)*0.9f
        val bgBot = if (isSelected) LColor(45f, 53f, 65f, 220f)*1.4f*0.9f  else LColor(38f, 41f, 50f, 180f)*0.9f
        
        lg.setImage(
            la.vok.LavokLibrary.Gradient.GradientInfo(
                bgTop, bgBot, 
                la.vok.LavokLibrary.Vectors.LPoint(0, 0), 
                la.vok.LavokLibrary.Vectors.LPoint(0, 50), 
                la.vok.LavokLibrary.Vectors.LPoint(1, 50)
            ).generate(),
            positionWithCache, size * 0.9f
        )
        
        // 2. Тени вместо обводки
        // Внутренняя тень
        lg.setImage(
            ShadowFrameInfo(
                (size * 0.9f).toLPoint() / 2,
                intensity = if (isSelected) 0.4f else 0.2f,
                spread = 3,
                color = if (isSelected) LColor(10f, 15f, 20f) else LColor.BLACK
            ).generate(),
            positionWithCache, size * 0.9f
        )

        lg.setImage(
            ShadowFrameInfo(
                (size * 0.9f).toLPoint(),
                intensity = 0.2f,
                spread = 4,
                type = ShadowType.OUTER
            ).generate(),
            positionWithCache, size * 0.9f + (la.vok.LavokLibrary.Vectors.Vec2(8f))
        )

        if (isSelected) {
            lg.setImage(
                ShadowFrameInfo(
                    (size * 0.9f).toLPoint() / 2,
                    intensity = 0.6f,
                    spread = 4,
                    color = LColor(60f, 80f, 100f),
                    type = ShadowType.OUTER
                ).generate(),
                positionWithCache, size * 0.9f + (la.vok.LavokLibrary.Vectors.Vec2(16f))
            )
        }
        
        lg.noStroke()
        slot?.item?.cellRender(lg, positionWithCache, size * 0.9f, this)
    }


    override fun callPostRender(lg: LGraphics) {
        if (!isVisible) return
    }

    override fun callResize() {}
}