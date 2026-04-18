package la.vok.Game.Windows

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.Game.GameContent.Items.Other.ItemSlot
import la.vok.LavokLibrary.LGraphics.LGraphics
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

    override fun callPreRender(lg: LGraphics) {
        if (!isVisible) return

        lg.fill(0f, 20f)
        for (i in 1..4) {
            lg.setBlock(positionWithCache, size * 0.9f * (1f + i/30f))
        }
    }

    override fun callRender(lg: LGraphics) {
        if (!isVisible) return

        when {
            isDragTarget -> lg.fill(220f, 230f, 255f, 160f)
            slot?.item?.isChoose == true -> lg.fill(190f, 200f, 210f, 130f)
            else -> lg.fill(140f, 150f, 160f, 100f)
        }
        lg.noStroke()
        lg.setBlock(positionWithCache, size*0.9f)
        slot?.item?.cellRender(lg, positionWithCache, size*0.9f, this)
    }


    override fun callPostRender(lg: LGraphics) {
        if (!isVisible) return
    }

    override fun callResize() {}
}