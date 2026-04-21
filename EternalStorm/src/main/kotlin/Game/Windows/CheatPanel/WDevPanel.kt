package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Input.KeyCode
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.FrameLimiter
import la.vok.Core.GameControllers.GameController
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.WStandartPanel
import la.vok.Game.GameContent.Crafts.CraftType
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.LLibs.AnimationType
import la.vok.LLibs.FloatAnimation
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.State.AppState
import processing.event.MouseEvent
import kotlin.math.pow
import kotlin.math.log10

class WDevPanel(windowsManager: WindowsManager, val gameController: GameController) : WStandartPanel(windowsManager) {

    override var tags: Array<String> get() = arrayOf("dev"); set(value) {}
    override var padding: Vec2 get() = 20 v 20; set(value) {}

    // =====================================================================
    // UI CONSTANTS & DESIGN TOKENS
    // =====================================================================

    private val panelW      = 780f
    private val panelH      = 760f
    private val cornerR     = 18f
    
    private val tabW        = 140f
    private val tabH        = 40f
    private val tabGap      = 12f
    
    private val cellW       = 560f
    private val cellH       = 58f
    private val cellGap     = 10f

    // =====================================================================
    // STATE & ANIMATION
    // =====================================================================

    private enum class Tab { ENTITIES, ITEMS, CRAFTS, DIMENSIONS, TECH }
    private var activeTab    = Tab.ENTITIES
    private var targetTab    = Tab.ENTITIES
    
    private val openAnim      = FloatAnimation(0f, 1f, AnimationType.EaseOut(3.5f))
    private var openProgress  = 0f
    private val openSpeed     = 4.0f
    
    private var scroll        = 0f
    private var targetScroll  = 0f
    private val scrollSmooth  = 12f

    private var hoveredIndex: Int = -1
    private var hoveredBtn: Int = -1 // 0 = main cell, 1 = action button, 2 = action button 2, 3 = tab
    
    private var isOpen = true

    // =====================================================================
    // MEMORY MONITORING
    // =====================================================================

    private var lastUsedMem     = 0L
    private var allocAcc        = 0L
    private var lastStatTime    = 0L
    private var allocRate       = 0f
    
    private val memHistory      = FloatArray(100)
    private var historyIdx      = 0
    private var historyTimer    = 0f

    private fun updateMemoryStats(dt: Float) {
        val rt = Runtime.getRuntime()
        val used = rt.totalMemory() - rt.freeMemory()
        val now = System.currentTimeMillis()

        // Track allocations (only positive deltas)
        if (used > lastUsedMem) {
            allocAcc += (used - lastUsedMem)
        }

        // Update allocation rate every second
        if (now - lastStatTime >= 1000) {
            allocRate = allocAcc.toFloat() / (1024f * 1024f)
            allocAcc = 0
            lastStatTime = now
        }

        // Update history graph every 0.1s
        historyTimer += dt
        if (historyTimer >= 0.1f) {
            memHistory[historyIdx] = used.toFloat() / (1024f * 1024f)
            historyIdx = (historyIdx + 1) % memHistory.size
            historyTimer = 0f
        }

        lastUsedMem = used
    }

    // =====================================================================
    // DATA PROVIDERS
    // =====================================================================

    private fun getList(): List<Any> = when (activeTab) {
        Tab.ENTITIES   -> gameController.coreController.objectRegistration.entities.values.filter { it.tag.isNotEmpty() }.toList()
        Tab.ITEMS      -> gameController.coreController.objectRegistration.items.values.toList()
        Tab.CRAFTS     -> gameController.gameCycle.craftApi.allCrafts
        Tab.DIMENSIONS -> gameController.gameCycle.dimensionsController.dimensions.values.sortedBy { it.dimensionTag }.toList()
        Tab.TECH       -> emptyList()
    }

    private fun getMaxScroll(): Float {
        val listSize = getList().size
        val effectiveCellH = if (activeTab == Tab.CRAFTS) cellH * 1.5f else cellH
        val totalHeight = listSize * (effectiveCellH + cellGap)
        val viewportH = panelH - 220f
        return (totalHeight - viewportH).coerceAtLeast(0f)
    }

    // =====================================================================
    // CORE DRAWING
    // =====================================================================

    override fun draw(mainRender: MainRender) {
        val dt = FrameLimiter.renderDeltaTime
        updateMemoryStats(dt)
        lg.pg.clear()
        
        if (isOpen) {
            openProgress = (openProgress + dt * openSpeed).coerceIn(0f, 1f)
        } else {
            openProgress = (openProgress - dt * openSpeed).coerceIn(0f, 1f)
        }
        
        if (openProgress <= 0f && !isOpen) {
            windowsManager.destroyWindow(this)
            return
        }

        scroll += (targetScroll - scroll) * (dt * scrollSmooth).coerceIn(0f, 1f)

        val alpha = openAnim.evaluate(openProgress)
        val scale = 0.85f + 0.15f * alpha
        
        drawPanelLayout(alpha, scale)
    }

    private fun drawPanelLayout(alpha: Float, scale: Float) {
        val cx = 0f
        val cy = 0f
        val w = panelW * scale
        val h = panelH * scale

        lg.noStroke()
        for (i in 1..4) {
            lg.fill(20f, 30f, 80f, (8f - i * 1.5f) * alpha)
            lg.setBlock(cx, cy, w + i * 12f, h + i * 12f)
        }

        lg.fill(16f, 18f, 26f, 240f * alpha)
        lg.setBlock(cx, cy, w, h)

        lg.fill(80f, 140f, 255f, 60f * alpha)
        lg.setBlock(cx, cy + h / 2f - 2f, w * 0.9f, 2f)

        lg.setTextAlign(0, 0)
        lg.fill(180f, 210f, 255f, 230f * alpha)
        lg.setText("DEVELOPER COMMAND CENTER", cx, cy + h / 2f - 35f * scale, 18f * scale)

        drawTabs(cx, cy, w, h, alpha, scale)

        val contentTopY = cy + h / 2f - 160f * scale
        val viewportH = h - 220f * scale
        
        when (activeTab) {
            Tab.ENTITIES   -> drawEntities(cx, contentTopY, viewportH, alpha, scale)
            Tab.ITEMS      -> drawItems(cx, contentTopY, viewportH, alpha, scale)
            Tab.CRAFTS     -> drawCrafts(cx, contentTopY - 20f * scale, viewportH, alpha, scale)
            Tab.DIMENSIONS -> drawDimensions(cx, contentTopY, viewportH, alpha, scale)
            Tab.TECH       -> drawTech(cx, contentTopY, viewportH, alpha, scale)
        }
    }

    private fun drawTabs(cx: Float, cy: Float, w: Float, h: Float, alpha: Float, scale: Float) {
        val tabs = Tab.values()
        val totalTabsW = tabs.size * tabW + (tabs.size - 1) * tabGap
        val startX = cx - (totalTabsW * scale) / 2f + (tabW * scale) / 2f
        val tabY = cy + h / 2f - 95f * scale

        tabs.forEachIndexed { i, tab ->
            val tx = startX + i * (tabW + tabGap) * scale
            val isActive = (tab == activeTab)
            val isHover = (hoveredIndex == i && hoveredBtn == 3)
            
            val bgAlpha = if (isActive) 180f else if (isHover) 120f else 60f
            lg.fill(40f, 60f, 120f, bgAlpha * alpha)
            lg.setBlock(tx, tabY, tabW * scale, tabH * scale)
            
            if (isActive) {
                lg.fill(100f, 180f, 255f, 255f * alpha)
                lg.setBlock(tx, tabY - tabH * scale / 2f + 2f, tabW * 0.6f * scale, 3f)
            }

            lg.fill(if (isActive) 255f else 180f, 240f * alpha)
            lg.setText(tab.name, tx, tabY + 4f, 13f * scale)
        }
    }

    private fun drawEntities(cx: Float, topY: Float, viewH: Float, alpha: Float, scale: Float) {
        val list = getList() as List<AbstractEntityType>
        val ch = cellH * scale
        val cw = cellW * scale
        val gap = cellGap * scale
        
        list.forEachIndexed { i, entity ->
            val posY = topY - i * (ch + gap) + scroll * scale
            if (posY > topY + ch || posY < topY - viewH - ch) return@forEachIndexed
            
            val isHover = hoveredIndex == i
            
            lg.fill(30f, 35f, 55f, 160f * alpha)
            if (isHover && hoveredBtn == 0) lg.fill(45f, 60f, 110f, 180f * alpha)
            lg.setBlock(cx, posY, cw, ch)
            
            lg.setTextAlign(-1, 0)
            lg.fill(220f, 235f, 255f, 240f * alpha)
            lg.setText(entity.tag, cx - cw / 2f + 25f * scale, posY + 4f, 15f * scale)
            
            val btnW = 110f * scale
            val btnX = cx + cw / 2f - btnW / 2f - 15f * scale
            val btnHover = (isHover && hoveredBtn == 1)
            
            lg.fill(60f, 180f, 120f, if (btnHover) 240f * alpha else 140f * alpha)
            lg.setBlock(btnX, posY, btnW, ch * 0.7f)
            
            lg.setTextAlign(0, 0)
            lg.fill(255f, 255f * alpha)
            lg.setText("SPAWN", btnX, posY + 4f, 12f * scale)
        }
    }

    private fun drawItems(cx: Float, topY: Float, viewH: Float, alpha: Float, scale: Float) {
        val list = getList() as List<AbstractItemType>
        val ch = cellH * scale
        val cw = cellW * scale
        val gap = cellGap * scale
        
        list.forEachIndexed { i, item ->
            val posY = topY - i * (ch + gap) + scroll * scale
            if (posY > topY + ch || posY < topY - viewH - ch) return@forEachIndexed
            
            val isHover = hoveredIndex == i
            
            lg.fill(32f, 32f, 48f, 160f * alpha)
            if (isHover && hoveredBtn == 0) lg.fill(50f, 50f, 85f, 180f * alpha)
            lg.setBlock(cx, posY, cw, ch)
            
            try {
                val sprite = gameController.coreController.spriteLoader.getValue(item.texture)
                lg.setImage(sprite, cx - cw / 2f + 30f * scale, posY, ch * 0.7f, ch * 0.7f)
            } catch (_: Exception) {}
            
            lg.setTextAlign(-1, 0)
            lg.fill(255f, 235f, 180f, 240f * alpha)
            lg.setText(item.tag, cx - cw / 2f + 70f * scale, posY + 4f, 14f * scale)
            
            val btnW = 90f * scale
            val btnX = cx + cw / 2f - btnW / 2f - 15f * scale
            val btnHover = (isHover && hoveredBtn == 1)
            
            lg.fill(200f, 130f, 60f, if (btnHover) 240f * alpha else 150f * alpha)
            lg.setBlock(btnX, posY, btnW, ch * 0.7f)
            
            lg.setTextAlign(0, 0)
            lg.fill(255f, 255f * alpha)
            lg.setText("GIVE", btnX, posY + 4f, 12f * scale)
        }
    }

    private fun drawCrafts(cx: Float, topY: Float, viewH: Float, alpha: Float, scale: Float) {
        val list = getList() as List<CraftType>
        val ch = cellH * 1.6f * scale
        val cw = cellW * scale
        val gap = cellGap * scale
        
        list.forEachIndexed { i, craft ->
            val posY = topY - i * (ch + gap) + scroll * scale
            if (posY > topY + ch || posY < topY - viewH - ch) return@forEachIndexed
            
            lg.fill(38f, 35f, 45f, 140f * alpha)
            lg.setBlock(cx, posY, cw, ch)
            
            val resType = craft.resultType
            if (resType != null) {
                try {
                    val sprite = gameController.coreController.spriteLoader.getValue(resType.texture)
                    lg.setImage(sprite, cx - cw / 2f + 40f * scale, posY, ch * 0.5f, ch * 0.5f)
                } catch (_: Exception) {}
                lg.setTextAlign(0, 0)
                lg.fill(200f, 220f, 255f, 200f * alpha)
                lg.setText("${craft.result.count}x", cx - cw / 2f + 40f * scale, posY - ch * 0.35f, 11f * scale)
            }
            
            lg.fill(160f, 180f, 220f, 180f * alpha)
            lg.setText("←", cx - cw / 2f + 90f * scale, posY + 6f, 22f * scale)
            
            var xOff = cx - cw / 2f + 145f * scale
            craft.ingredientTypes.forEach { (type, count) ->
                try {
                    val sprite = gameController.coreController.spriteLoader.getValue(type.texture)
                    lg.setImage(sprite, xOff, posY, ch * 0.45f, ch * 0.45f)
                } catch (_: Exception) {}
                lg.fill(255f, 180f, 120f, 220f * alpha)
                lg.setText("${count}x", xOff, posY - ch * 0.35f, 10f * scale)
                xOff += 48f * scale
            }
        }
    }

    private fun drawDimensions(cx: Float, topY: Float, viewH: Float, alpha: Float, scale: Float) {
        val list = getList() as List<AbstractDimension>
        val ch = cellH * scale
        val cw = cellW * scale
        val gap = cellGap * scale
        
        list.forEachIndexed { i, dim ->
            val posY = topY - i * (ch + gap) + scroll * scale
            if (posY > topY + ch || posY < topY - viewH - ch) return@forEachIndexed
            
            val isHover = hoveredIndex == i
            
            lg.fill(30f, 45f, 50f, 160f * alpha)
            if (isHover && hoveredBtn == 0) lg.fill(50f, 70f, 85f, 180f * alpha)
            lg.setBlock(cx, posY, cw, ch)
            
            lg.setTextAlign(-1, 0)
            lg.fill(150f, 255f, 200f, 240f * alpha)
            lg.setText(dim.dimensionTag, cx - cw / 2f + 25f * scale, posY + 4f, 16f * scale)
            
            lg.fill(180f, 200f, 220f, 220f * alpha)
            lg.setText("Entities: ${dim.entitySystem.entities.size}", cx - cw / 2f + 340f * scale, posY + 4f, 12f * scale)
            
            val btnW = 110f * scale
            val btnX = cx + cw / 2f - btnW / 2f - 15f * scale
            val btnHover = (isHover && hoveredBtn == 1)
            
            lg.fill(80f, 140f, 255f, if (btnHover) 240f * alpha else 150f * alpha)
            lg.setBlock(btnX, posY, btnW, ch * 0.7f)
            
            lg.setTextAlign(0, 0)
            lg.fill(255f, 255f * alpha)
            lg.setText("TELEPORT", btnX, posY + 4f, 11f * scale)
        }
    }

    private fun drawTech(cx: Float, topY: Float, viewH: Float, alpha: Float, scale: Float) {
        val tw = panelW * 0.82f * scale
        var yOff = topY - 30f * scale + scroll * scale
        
        val rt = Runtime.getRuntime()
        val mb = 1024 * 1024
        val used = (rt.totalMemory() - rt.freeMemory()) / mb
        val max = rt.maxMemory() / mb
        val pct = (used.toFloat() / max).coerceIn(0f, 1f)
        
        lg.setTextAlign(-1, 0)
        lg.fill(140f, 200f, 255f, 220f * alpha)
        lg.setText("MEMORY ALLOCATION", cx - tw / 2f + 10f * scale, yOff, 15f * scale)
        yOff -= 32f * scale
        
        lg.fill(25f, 30f, 45f, 200f * alpha)
        lg.setBlock(cx, yOff, tw, 28f * scale)
        lg.fill(80f, 160f, 255f, 180f * alpha)
        val barW = tw * pct
        lg.setBlock(cx - tw / 2f + barW / 2f, yOff, barW, 28f * scale)
        
        lg.fill(255f, 240f * alpha)
        lg.setText("Used: $used MB / Max: $max MB", cx - tw / 2f + 15f * scale, yOff + 4f, 11f * scale)
        lg.setTextAlign(1, 0)
        lg.fill(255f, 150f, 100f, 220f * alpha)
        lg.setText("ALLOC: ${"%.1f".format(allocRate)} MB/s", cx + tw / 2f - 15f * scale, yOff + 4f, 11f * scale)
        
        // --- MEMORY GRAPH ---
        yOff -= 55f * scale
        val graphH = 60f * scale
        lg.fill(20f, 25f, 40f, 180f * alpha)
        lg.setBlock(cx, yOff, tw, graphH)
        
        // Draw history lines
        lg.stroke(80f, 160f, 255f, 200f * alpha)
        lg.strokeWeight(2f * scale)
        val step = tw / (memHistory.size - 1)
        val minMem = memHistory.minOrNull() ?: 0f
        val maxMem = memHistory.maxOrNull()?.coerceAtLeast(minMem + 1f) ?: 100f
        val range = maxMem - minMem
        
        for (i in 0 until memHistory.size - 1) {
            val idx1 = (historyIdx + i) % memHistory.size
            val idx2 = (historyIdx + i + 1) % memHistory.size
            
            val v1 = memHistory[idx1]
            val v2 = memHistory[idx2]
            
            val x1 = cx - tw / 2f + i * step
            val x2 = cx - tw / 2f + (i + 1) * step
            val y1 = yOff - graphH / 2f + ((v1 - minMem) / range) * graphH
            val y2 = yOff - graphH / 2f + ((v2 - minMem) / range) * graphH
            
            lg.setLine(x1, y1, x2, y2)
        }
        lg.noStroke()
        lg.setTextAlign(-1, 0)

        yOff -= 50f * scale
        
        lg.fill(140f, 200f, 255f, 220f * alpha)
        lg.setText("SYSTEM TELEMETRY", cx - tw / 2f + 10f * scale, yOff, 15f * scale)
        yOff -= 32f * scale
        
        val stats = listOf(
            "Frame Rate" to "${FrameLimiter.currentRenderFPS} FPS",
            "Tick Time"  to "${"%.3f".format(FrameLimiter.renderDeltaTime * 1000)} ms",
            "Avg Entities" to "${gameController.gameCycle.entityApi.getAllAcrossDimensions().size}",
            "Uptime"     to "${(FrameLimiter.uptimeMs / 1000) / 60}m ${ (FrameLimiter.uptimeMs / 1000) % 60}s"
        )
        
        stats.forEach { (k, v) ->
            lg.fill(160f, 170f, 185f, 200f * alpha)
            lg.setText(k, cx - tw / 2f + 20f * scale, yOff, 13f * scale)
            lg.fill(210f, 230f, 255f, 255f * alpha)
            lg.setText(v, cx + 50f * scale, yOff, 13f * scale)
            yOff -= 26f * scale
        }

        yOff -= 40f * scale

        lg.fill(140f, 200f, 255f, 220f * alpha)
        lg.setText("DEBUG OVERLAYS", cx - tw / 2f + 10f * scale, yOff, 15f * scale)
        yOff -= 45f * scale
        
        val btnW = 240f * scale
        val btnH = 45f * scale
        
        val hActive = AppState.hitboxDebug
        val hHover = hoveredIndex == 10 && hoveredBtn == 1
        lg.fill(if (hActive) 60f else 30f, if (hActive) 180f else 40f, if (hActive) 100f else 55f, if (hHover) 255f * alpha else 180f * alpha)
        lg.setBlock(cx - btnW / 2f - 10f * scale, yOff, btnW, btnH)
        lg.setTextAlign(0, 0)
        lg.fill(255f, 255f * alpha)
        lg.setText("HITBOXES: ${if (hActive) "ACTIVE" else "DISABLED"}", cx - btnW / 2f - 10f * scale, yOff + 4f, 12f * scale)
        
        val rActive = AppState.renderDebug
        val rHover = hoveredIndex == 11 && hoveredBtn == 1
        lg.fill(if (rActive) 100f else 30f, if (rActive) 140f else 40f, if (rActive) 255f else 55f, if (rHover) 255f * alpha else 180f * alpha)
        lg.setBlock(cx + btnW / 2f + 10f * scale, yOff, btnW, btnH)
        lg.fill(255f, 255f * alpha)
        lg.setText("METADATA: ${if (rActive) "VISIBLE" else "HIDDEN"}", cx + btnW / 2f + 10f * scale, yOff + 4f, 12f * scale)

        yOff -= 55f * scale
        
        val bActive = AppState.enableBatching
        val bHover = hoveredIndex == 12 && hoveredBtn == 1
        lg.fill(if (bActive) 30f else 100f, if (bActive) 140f else 40f, if (bActive) 100f else 55f, if (bHover) 255f * alpha else 180f * alpha)
        lg.setBlock(cx - btnW / 2f - 10f * scale, yOff, btnW, btnH)
        lg.fill(255f, 255f * alpha)
        lg.setText("BATCH: ${if (bActive) "ACTIVE" else "DISABLED"}", cx - btnW / 2f - 10f * scale, yOff + 4f, 12f * scale)
        
        val dbActive = AppState.debugBatchGrid
        val dbHover = hoveredIndex == 13 && hoveredBtn == 1
        lg.fill(if (dbActive) 100f else 30f, if (dbActive) 140f else 40f, if (dbActive) 255f else 55f, if (dbHover) 255f * alpha else 180f * alpha)
        lg.setBlock(cx + btnW / 2f + 10f * scale, yOff, btnW, btnH)
        lg.fill(255f, 255f * alpha)
        lg.setText("BATCH GRID: ${if (dbActive) "VISIBLE" else "HIDDEN"}", cx + btnW / 2f + 10f * scale, yOff + 4f, 12f * scale)

        yOff -= 75f * scale

        lg.setTextAlign(-1, 0)
        lg.fill(140f, 200f, 255f, 220f * alpha)
        lg.setText("CAMERA ZOOM: ${"%.1f".format(gameController.mainCamera.size)}", cx - tw / 2f + 10f * scale, yOff, 15f * scale)
        yOff -= 32f * scale
        
        lg.fill(30f, 40f, 60f, 150f * alpha)
        lg.setBlock(cx, yOff, tw, 20f * scale)
        
        val minZ = 0.5f
        val maxZ = 200f
        val zNorm = (log10(gameController.mainCamera.size / minZ) / log10(maxZ / minZ)).coerceIn(0f, 1f)
        val hx = cx - tw / 2f + tw * zNorm
        lg.fill(120f, 180f, 255f, 230f * alpha)
        lg.setBlock(hx, yOff, 30f * scale, 30f * scale)
    }

    override fun mouseWheel(position: Vec2, event: MouseEvent) {
        val delta = event.count.toFloat() * 85f
        targetScroll = (targetScroll + delta).coerceIn(0f, getMaxScroll())
    }

    override fun mouseUpdate(position: Vec2, oldPosition: Vec2) {
        if (!isOpen) return
        
        hoveredIndex = -1
        hoveredBtn = -1
        
        val alpha = openAnim.evaluate(openProgress)
        val scale = 0.85f + 0.15f * alpha
        val cx = 0f
        val cy = 0f
        val w = panelW * scale
        val h = panelH * scale
        
        val tabs = Tab.values()
        val totalTabsW = tabs.size * tabW + (tabs.size - 1) * tabGap
        val startX = cx - (totalTabsW * scale) / 2f + (tabW * scale) / 2f
        val tabY = cy + h / 2f - 95f * scale
        
        tabs.forEachIndexed { i, _ ->
            val tx = startX + i * (tabW + tabGap) * scale
            if (absInside(tx v tabY, tabW * scale v tabH * scale, position)) {
                hoveredIndex = i
                hoveredBtn = 3
                return
            }
        }
        
        val topY = cy + h / 2f - 160f * scale
        val viewH = h - 220f * scale
        val cw = cellW * scale
        val gap = cellGap * scale

        if (activeTab == Tab.TECH) {
            val tw = panelW * 0.82f * scale
            val btnW = 240f * scale
            val btnH = 45f * scale
            var yOff = (topY - (30 + 32 + 55 + 50 + 32 + 4 * 26 + 40 + 45) * scale) + scroll * scale
            
            if (absInside(cx - btnW / 2f - 10f * scale v yOff, btnW v btnH, position)) { hoveredIndex = 10; hoveredBtn = 1; return }
            if (absInside(cx + btnW / 2f + 10f * scale v yOff, btnW v btnH, position)) { hoveredIndex = 11; hoveredBtn = 1; return }
            
            yOff -= 55f * scale
            if (absInside(cx - btnW / 2f - 10f * scale v yOff, btnW v btnH, position)) { hoveredIndex = 12; hoveredBtn = 1; return }
            if (absInside(cx + btnW / 2f + 10f * scale v yOff, btnW v btnH, position)) { hoveredIndex = 13; hoveredBtn = 1; return }

            yOff -= (75 + 32) * scale
            if (absInside(cx v yOff, tw v 40f * scale, position)) { hoveredIndex = 20; hoveredBtn = 1; return }
            return
        }
        
        val list = getList()
        val ch = (if (activeTab == Tab.CRAFTS) cellH * 1.6f else cellH) * scale
        val topYAdj = if (activeTab == Tab.CRAFTS) topY - 20f * scale else topY
        
        list.forEachIndexed { i, _ ->
            val posY = topYAdj - i * (ch + gap) + scroll * scale
            if (posY > topYAdj + ch || posY < topYAdj - viewH - ch) return@forEachIndexed
            
            if (absInside(cx v posY, cw v ch, position)) {
                hoveredIndex = i
                hoveredBtn = 0
                
                val btnW = (if (activeTab == Tab.ITEMS) 90f else 110f) * scale
                val btnX = cx + cw / 2f - btnW / 2f - 15f * scale
                if (absInside(btnX v posY, btnW v ch * 0.7f, position)) {
                    hoveredBtn = 1
                }
                return
            }
        }
    }

    override fun leftPressed(position: Vec2) {
        if (!isOpen) return
        
        if (hoveredBtn == 3) {
            activeTab = Tab.values()[hoveredIndex]
            targetScroll = 0f
            scroll = 0f
            return
        }
        
        if (hoveredIndex != -1) {
            when (activeTab) {
                Tab.ENTITIES -> if (hoveredBtn == 1) spawnEntity(getList()[hoveredIndex] as AbstractEntityType)
                Tab.ITEMS    -> if (hoveredBtn == 1) giveItem(getList()[hoveredIndex] as AbstractItemType)
                Tab.DIMENSIONS -> if (hoveredBtn == 1) teleportToDim(getList()[hoveredIndex] as AbstractDimension)
                Tab.TECH -> {
                    if (hoveredIndex == 10) AppState.hitboxDebug = !AppState.hitboxDebug
                    if (hoveredIndex == 11) AppState.renderDebug = !AppState.renderDebug
                    if (hoveredIndex == 12) {
                        AppState.enableBatching = !AppState.enableBatching
                        if (!AppState.enableBatching) {
                            gameController.gameCycle.batchApi.clearAll()
                        }
                    }
                    if (hoveredIndex == 13) AppState.debugBatchGrid = !AppState.debugBatchGrid
                    if (hoveredIndex == 20) {
                        val scale = 0.85f + 0.15f * openAnim.evaluate(openProgress)
                        val tw = panelW * 0.82f * scale
                        val cx = 0f
                        val minZ = 0.5f
                        val maxZ = 200f
                        val zNorm = ((position.x - (cx - tw / 2f)) / (tw.coerceAtLeast(1f))).coerceIn(0f, 1f)
                        val targetZoom = minZ * (maxZ / minZ).pow(zNorm)
                        gameController.mainCamera.setCameraZoom(targetZoom, minZoom = minZ, maxZoom = maxZ)
                    }
                }
                else -> {}
            }
        }
    }

    override fun leftUpdate(position: Vec2, oldPosition: Vec2) {
        if (activeTab == Tab.TECH && hoveredIndex == 20) {
            val scale = 0.85f + 0.15f * openAnim.evaluate(openProgress)
            val tw = panelW * 0.82f * scale
            val cx = 0f
            
            val minZ = 0.5f
            val maxZ = 200f
            val zNorm = ((position.x - (cx - tw / 2f)) / (tw.coerceAtLeast(1f))).coerceIn(0f, 1f)
            val targetZoom = minZ * (maxZ / minZ).pow(zNorm)
            gameController.mainCamera.setCameraZoom(targetZoom, minZoom = minZ, maxZoom = maxZ)
        }
    }
    private fun absInside(pos: Vec2, size: Vec2, tap: Vec2): Boolean {
        return tap.x >= pos.x - size.x / 2f && tap.x <= pos.x + size.x / 2f &&
               tap.y >= pos.y - size.y / 2f && tap.y <= pos.y + size.y / 2f
    }

    private fun spawnEntity(type: AbstractEntityType) {
        val player = gameController.playerControl.getPlayerEntity() ?: return
        val dim = player.dimension ?: return
        val forward = player.facing.toFloat()
        val spawnPos = player.position + (forward * 30f v 0f)
        gameController.gameCycle.entityApi.spawnEntity(dim, type.tag, spawnPos)
    }

    private fun giveItem(type: AbstractItemType) {
        val player = gameController.playerControl.getPlayerEntity() ?: return
        val dim = player.dimension ?: return
        val item = gameController.gameCycle.itemsApi.getRegisteredItem(type, 1)
        val remaining = player.inventory?.itemContainer?.addItem(item) ?: 1
        if (remaining > 0) {
            gameController.gameCycle.itemsApi.spawnItemEntity(dim, item.copy().apply { count = remaining }, player.position, true)
        }
    }

    private fun teleportToDim(target: AbstractDimension) {
        val player = gameController.playerControl.getPlayerEntity() ?: return
        val currentDim = player.dimension ?: return
        gameController.gameCycle.entityApi.teleport(player, target, target.width/2f v target.height)
        gameController.gameCycle.dimensionsApi.changeRenderDimension(currentDim, target)
    }

    fun toggle() {
        isOpen = !isOpen
        if (isOpen) {
            targetScroll = 0f
            scroll = 0f
        }
    }

    override fun keyPressed(key: Int) {
        if (key == KeyCode.F1 || key == 27) toggle() // F1 or ESC
    }
}