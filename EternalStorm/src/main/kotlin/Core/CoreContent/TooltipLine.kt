package la.vok.Core.CoreControllers.Parts

import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.Vec2

sealed class TooltipLine {
    abstract fun render(lg: LGraphics, pos: Vec2, width: Float): Float
    abstract fun getWidth(lg: LGraphics): Float

    data class Title(val text: String, val color: LColor = LColor(255f, 230f, 100f)) : TooltipLine() {
        override fun render(lg: LGraphics, pos: Vec2, width: Float): Float {
            lg.fill(color)
            lg.setTextAlign(-1, -1)
            lg.setText(text, pos.x, pos.y, 18f)
            return 24f
        }
        override fun getWidth(lg: LGraphics) = lg.getTextWidth(text, 18f)
    }

    data class Description(val text: String) : TooltipLine() {
        override fun render(lg: LGraphics, pos: Vec2, width: Float): Float {
            lg.fill(200f)
            lg.setTextAlign(-1, -1)
            lg.setText(text, pos.x, pos.y, 14f)
            return 18f
        }
        override fun getWidth(lg: LGraphics) = lg.getTextWidth(text, 14f)
    }

    data class Stat(val label: String, val value: String, val color: LColor = LColor(150f, 255f, 150f)) : TooltipLine() {
        override fun render(lg: LGraphics, pos: Vec2, width: Float): Float {
            lg.fill(220f)
            lg.setTextAlign(-1, -1)
            lg.setText(label, pos.x, pos.y, 14f)
            
            lg.fill(color)
            lg.setTextAlign(1, -1)
            lg.setText(value, pos.x + width, pos.y, 14f)
            return 18f
        }
        override fun getWidth(lg: LGraphics) = lg.getTextWidth("$label $value", 14f) + 20f
    }

    object Separator : TooltipLine() {
        override fun render(lg: LGraphics, pos: Vec2, width: Float): Float {
            lg.stroke(255f, 30f)
            lg.strokeWeight(1f)
            lg.setLine(pos.x, pos.y - 12f, pos.x + width, pos.y - 12f)
            lg.noStroke()
            return 20f
        }
        override fun getWidth(lg: LGraphics) = 0f
    }
}
