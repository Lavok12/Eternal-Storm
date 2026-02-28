package la.vok.Game.GameContent.Windows

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
    var cellType: InventoryCellType = InventoryCellType.STANDART,
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
    override fun callPreRender(lg: LGraphics) {}

    override fun callRender(lg: LGraphics) {
        lg.fill(140f, 150f, 160f, 100f)
        if (slot?.item?.isChoose ?: false) {
            lg.fill(190f,200f,210f,130f)
        }
        lg.noStroke()
        lg.setBlock(positionWithCache, size, 15f)

        slot?.item?.cellRender(lg, positionWithCache, size, this)
    }

    override fun callPostRender(lg: LGraphics) {}

    override fun callResize() {}
}