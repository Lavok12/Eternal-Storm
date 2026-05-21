package la.vok.Game.Windows.GameUI.Modules

import la.vok.Core.CoreContent.Windows.Modules.IUiModule
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.Game.GameController.PlayerControl
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class StatsModule(val playerControl: PlayerControl) : IUiModule {
    override val id: String = "stats"
    override var isEnabled: Boolean = true

    // ══════════════════════════════════════════════════════
    // CONFIG
    // ══════════════════════════════════════════════════════

    private val barWidth   = 200f
    private val barHeight  = 18f
    private val margin     = 4f
    private val padding    = 4f
    private val fontSize   = 4f

    // background panel size
    private val panelW = barWidth + padding * 2f + padding
    private val panelH = barHeight + padding * 2f

    /** Top-right corner position (in logical coords) */
    private fun panelPos(window: AbstractWindow): Vec2 =
        (window.logicalSize.x / 2f - margin - panelW / 2f) v
        (window.logicalSize.y / 2f - margin - panelH / 2f)

    // Hit-area element (used by insideUxElement for click blocking)
    private var hitElement: WindowElement? = null

    // ══════════════════════════════════════════════════════
    // LIFECYCLE
    // ══════════════════════════════════════════════════════

    override fun onAttach(window: AbstractWindow) {
        val pos = panelPos(window)
        hitElement = WindowElement(
            window   = window,
            position = pos,
            size     = panelW v panelH,
            align    = Vec2.ZERO
        )
        window.windowElements += hitElement!!
    }

    override fun onDetach(window: AbstractWindow) {
        hitElement?.let { window.windowElements.remove(it) }
        hitElement = null
    }

    override fun update(window: AbstractWindow) {
        // keep hit-element aligned when window resizes
        val pos = panelPos(window)
        hitElement?.let {
            it.position = pos
            it.markDirty()
        }
    }

    override fun postDraw(window: AbstractWindow, lg: LGraphics) {
        val player = playerControl.getPlayerEntity() ?: return
        val hpBody = player.hpBody ?: return

        val pos = panelPos(window)
        val px = pos.x
        val py = pos.y

        // ─── Background ───────────────────────────────────
        lg.noStroke()
        lg.fill(10f, 10f, 18f, 180f)
        lg.setBlock(px, py, panelW, panelH)

        // ─── HP bar track ─────────────────────────────────
        val barX = px - panelW / 2f + padding + padding + barWidth / 2f
        val barY = py

        lg.fill(40f, 40f, 55f, 220f)
        lg.setBlock(barX, barY, barWidth, barHeight * 0.6f)

        // ─── HP bar fill ──────────────────────────────────
        val pct = hpBody.percentageOfHp().coerceIn(0f, 1f)
        val fillW = (barWidth - 2f) * pct
        if (fillW > 1f) {
            val hue = pct               // 0 = red, 1 = green
            val r = 255f * (1f - hue)
            val g = 200f * hue + 55f
            lg.fill(r, g, 60f, 230f)
            val fillX = barX - barWidth / 2f + 1f + fillW / 2f
            lg.setBlock(fillX, barY, fillW, barHeight - 2f)
        }
    }
}
