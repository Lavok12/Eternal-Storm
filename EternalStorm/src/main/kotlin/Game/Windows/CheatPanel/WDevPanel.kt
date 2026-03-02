package la.vok.Game.ClientContent.Windows

import la.vok.Core.CoreContent.Input.KeyCode
import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.WStandartPanel
import la.vok.Core.CoreControllers.CoreContent.Windows.ElementsStrorage.WindowElement
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.Core.FrameLimiter
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.LLibs.AnimationType
import la.vok.LLibs.FloatAnimation
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import processing.event.MouseEvent

class WDevPanel(windowsManager: WindowsManager, val gameController: GameController) : WStandartPanel(windowsManager) {

    override var tags: Array<String> get() = arrayOf("dev"); set(value) {}
    override var padding: Vec2 get() = 20 v 20; set(value) {}

    // =====================================================================
    // НАСТРОЙКИ
    // =====================================================================

    private val tabAnim        = FloatAnimation(0f, 1f, AnimationType.EaseInOut(3f))
    private val openAnim       = FloatAnimation(0f, 1f, AnimationType.EaseOut(3f))
    private val hoverAnim      = FloatAnimation(0f, 1f, AnimationType.EaseOut(4f))
    private val listScrollAnim = FloatAnimation(0f, 1f, AnimationType.EaseInOut(3f))

    private val cellW       = 260f
    private val cellH       = 44f
    private val cellGap     = 6f
    private val tabH        = 38f
    private val tabW        = 160f
    private val panelW      = 580f
    private val panelH      = 680f
    private val scrollSpeed = 5f
    private val animSpeed   = 5f

    // =====================================================================
    // СОСТОЯНИЕ
    // =====================================================================

    private enum class Tab { ENTITIES, ITEMS }
    private var activeTab    = Tab.ENTITIES
    private var targetTab    = Tab.ENTITIES
    private var tabProgress  = 1f   // 0 → старая вкладка, 1 → новая
    private var openProgress = 0f
    private var isOpen       = true

    private var entityScroll  = 0f
    private var itemScroll    = 0f
    private var targetEntityScroll = 0f
    private var targetItemScroll   = 0f

    private var hoveredEntity: AbstractEntityType? = null
    private var hoveredItem:   AbstractItemType?   = null
    private var hoverAlpha = 0f

    private val allEntities: List<AbstractEntityType>
        get() = gameController.coreController.objectRegistration.entities.values
            .filter { it.tag.isNotEmpty() }
            .toList()

    private val allItems: List<AbstractItemType>
        get() = gameController.coreController.objectRegistration.items.values.toList()

    // =====================================================================
    // LIFECYCLE
    // =====================================================================

    private fun drawTabs(cx: Float, cy: Float, w: Float, h: Float, alpha: Float, scale: Float) {
        val tabY = cy + h / 2f - tabH * scale / 2f - 12f * scale
        val tabs = Tab.values()

        tabs.forEachIndexed { i, tab ->
            val tx = cx - w / 2f + 24f * scale + i * (tabW * scale + 8f * scale) + tabW * scale / 2f
            val isActive = tab == (if (tabProgress >= 0.5f) targetTab else activeTab)

            // Фон таба
            if (isActive) {
                lg.fill(60f, 100f, 220f, 180f * alpha)
            } else {
                lg.fill(30f, 35f, 50f, 160f * alpha)
            }
            lg.setBlock(tx, tabY, tabW * scale, tabH * scale, 10f)

            // Подчёркивание активного таба
            if (isActive) {
                lg.fill(120f, 180f, 255f, 200f * alpha)
                lg.setBlock(tx, tabY - tabH * scale / 2f + 3f, tabW * scale * 0.6f, 2f, 1f)
            }

            val label = when (tab) {
                Tab.ENTITIES -> "ENTITIES"
                Tab.ITEMS    -> "ITEMS"
            }
            lg.fill(if (isActive) 240f else 160f, 200f * alpha)
            lg.setText(label, tx, tabY + 6f, 18f)
        }
    }

    override fun draw(mainRender: MainRender) {
        super.draw(mainRender)
        lg.fill(0f)
        lg.pg.clear()
        val dt = FrameLimiter.renderDeltaTime

        // Анимация открытия
        val targetOpen = if (isOpen) 1f else 0f
        openProgress += (targetOpen - openProgress) * (dt * animSpeed).coerceIn(0f, 1f)

        // Плавный скролл
        entityScroll += (targetEntityScroll - entityScroll) * (dt * scrollSpeed * 2).coerceIn(0f, 1f)
        itemScroll   += (targetItemScroll   - itemScroll)   * (dt * scrollSpeed * 2).coerceIn(0f, 1f)

        // Анимация смены вкладки
        if (tabProgress < 1f) {
            tabProgress = (tabProgress + dt * animSpeed).coerceIn(0f, 1f)
            if (tabProgress >= 1f) activeTab = targetTab
        }

        if (openProgress < 0.01f) return

        val alpha = openProgress
        val scale = 0.85f + 0.15f * openProgress
        val cx = 0f
        val cy = 0f

        drawPanel(cx, cy, alpha, scale)
    }

    private fun drawPanel(cx: Float, cy: Float, alpha: Float, scale: Float) {
        val w = panelW * scale
        val h = panelH * scale

        // Тень панели
        for (i in 1..6) {
            lg.noStroke()
            lg.fill(0f, 15f * alpha)
            lg.setBlock(cx, cy, w + i * 8f, h + i * 8f, 22f)
        }

        // Фон панели
        lg.fill(18f, 20f, 28f, 230f * alpha)
        lg.setBlock(cx, cy, w, h, 18f)

        // Акцентная линия сверху
        lg.fill(80f, 140f, 255f, 180f * alpha)
        lg.setBlock(cx, cy + h / 2f - 2f, w * 0.7f, 3f, 2f)

        // Заголовок
        lg.fill(220f, 230f, 255f, 200f * alpha)
        lg.setText("DEV PANEL", cx - w / 2f + 40f, -cy - h / 2f + 22f, 14f)

        drawTabs(cx, cy, w, h, alpha, scale)

        val contentY = cy + h / 2f - tabH * scale - 60f * scale
        val contentH = h - tabH * scale - 80f * scale

        when (if (tabProgress < 0.5f) activeTab else targetTab) {
            Tab.ENTITIES -> drawEntityList(cx, contentY, w, contentH, alpha, scale)
            Tab.ITEMS    -> drawItemList(cx, contentY, w, contentH, alpha, scale)
        }

        // Превью при ховере
        drawPreview(cx, cy, w, h, alpha, scale)
    }

    private fun drawEntityList(cx: Float, topY: Float, w: Float, h: Float, alpha: Float, scale: Float) {
        val list   = allEntities
        val cellHs = cellH * scale
        val cellWs = cellW * scale
        val gapS   = cellGap * scale

        list.forEachIndexed { i, entity ->
            val y = topY - i * (cellHs + gapS) - entityScroll * scale
            if (y < topY - h - cellHs || y > topY + cellHs) return@forEachIndexed

            val isHover = hoveredEntity === entity

            // Фон ячейки на всю ширину
            lg.fill(if (isHover) 50f else 25f, if (isHover) 80f else 35f, if (isHover) 160f else 55f,
                if (isHover) 200f * alpha else 120f * alpha)
            lg.setBlock(cx, y, cellWs, cellHs, 10f)

            // Левый акцент
            lg.fill(80f, 140f, 255f, if (isHover) 255f * alpha else 80f * alpha)
            lg.setBlock(cx - cellWs / 2f + 3f, y, 3f, cellHs * 0.6f, 2f)

            // Тег — слева, чуть ниже центра
            lg.setTextAlign(-1, 0)
            lg.fill(200f, 220f, 255f, 210f * alpha)
            lg.setText(entity.tag, cx - cellWs / 2f + 18f, y - cellHs * 0.1f + 5f, 14f)

            // Кнопка SPAWN — правая часть, на всю высоту ячейки
            val btnW = cellWs * 0.28f
            val btnX = cx + cellWs / 2f - btnW / 2f - 6f * scale
            lg.fill(40f, 180f, 100f, if (isHover) 220f * alpha else 140f * alpha)
            lg.setBlock(btnX, y, btnW, cellHs * 0.75f, 8f)
            lg.setTextAlign(0, 0)
            lg.fill(220f, 255f, 230f, 220f * alpha)
            lg.setText("SPAWN", btnX, y - cellHs * 0.1f + 5f, 12f)

            lg.setTextAlign(0, 0) // сброс
        }
    }

    private fun drawItemList(cx: Float, topY: Float, w: Float, h: Float, alpha: Float, scale: Float) {
        val list   = allItems
        val cellHs = cellH * scale
        val cellWs = cellW * scale
        val gapS   = cellGap * scale

        list.forEachIndexed { i, item ->
            val y = topY - i * (cellHs + gapS) - itemScroll * scale
            if (y < topY - h - cellHs || y > topY + cellHs) return@forEachIndexed

            val isHover = hoveredItem === item

            lg.fill(if (isHover) 50f else 25f, if (isHover) 60f else 35f, if (isHover) 80f else 55f,
                if (isHover) 200f * alpha else 120f * alpha)
            lg.setBlock(cx, y, cellWs, cellHs, 10f)

            // Левый акцент
            lg.fill(200f, 160f, 80f, if (isHover) 255f * alpha else 80f * alpha)
            lg.setBlock(cx - cellWs / 2f + 3f, y, 3f, cellHs * 0.6f, 2f)

            // Иконка
            try {
                val sprite = gameController.coreController.spriteLoader.getValue(item.sprite)
                lg.setImage(sprite, cx - cellWs / 2f + 24f, y, cellHs * 0.7f, cellHs * 0.7f)
            } catch (_: Exception) {}

            // Тег — слева после иконки
            lg.setTextAlign(-1, 0)
            lg.fill(220f, 200f, 160f, 210f * alpha)
            lg.setText(item.tag, cx - cellWs / 2f + 48f, y - cellHs * 0.1f + 5f, 11f)

            // Кнопка GIVE
            val btnW = cellWs * 0.22f
            val btnX = cx + cellWs / 2f - btnW / 2f - 6f * scale
            lg.fill(180f, 120f, 40f, if (isHover) 220f * alpha else 140f * alpha)
            lg.setBlock(btnX, y, btnW, cellHs * 0.75f, 8f)
            lg.setTextAlign(0, 0)
            lg.fill(255f, 230f, 180f, 220f * alpha)
            lg.setText("GIVE", btnX, y - cellHs * 0.1f + 4f, 11f)

            lg.setTextAlign(0, 0)
        }
    }
    private fun drawPreview(cx: Float, cy: Float, w: Float, h: Float, alpha: Float, scale: Float) {
        val entity = hoveredEntity
        val item   = hoveredItem
        if (entity == null && item == null) return

        val previewX = cx + w / 2f + 20f
        val previewY = cy
        val previewW = 180f * scale
        val previewH = 180f * scale

        lg.fill(18f, 20f, 28f, 200f * alpha)
        lg.setBlock(previewX + previewW / 2f, previewY, previewW, previewH, 14f)

        if (item != null) {
            try {
                val sprite = gameController.coreController.spriteLoader.getValue(item.sprite)
                lg.setImage(sprite, previewX + previewW / 2f, previewY, previewW * 0.7f, previewH * 0.7f)
            } catch (_: Exception) {}
            lg.fill(200f, 180f, 130f, 200f * alpha)
            lg.setText(item.tag, previewX + previewW / 2f, previewY - previewH / 2f + 14f + 4f, 12f)
        } else if (entity != null && entity.imgPreview.isNotEmpty()) {
            try {
                val sprite = gameController.coreController.spriteLoader.getValue(entity.imgPreview)
                lg.setImage(sprite, previewX + previewW / 2f, previewY, previewW * 0.7f, previewH * 0.7f)
            } catch (_: Exception) {}
            lg.fill(180f, 210f, 255f, 200f * alpha)
            lg.setText(entity.tag, previewX + previewW / 2f, previewY - previewH / 2f + 14f + 4f, 12f)
        }
    }

    // =====================================================================
    // ВВОД — мышь
    // =====================================================================

    override fun mouseWheel(position: Vec2, event: MouseEvent) {
        if (!isOpen) return
        val delta = event.count.toFloat() * 60f
        when (activeTab) {
            Tab.ENTITIES -> {
                val maxScroll = ((allEntities.size * (cellH + cellGap)) - panelH * 0.6f).coerceAtLeast(0f)
                targetEntityScroll = (targetEntityScroll + delta).coerceIn(0f, maxScroll)
            }
            Tab.ITEMS -> {
                val maxScroll = ((allItems.size * (cellH + cellGap)) - panelH * 0.6f).coerceAtLeast(0f)
                targetItemScroll = (targetItemScroll + delta).coerceIn(0f, maxScroll)
            }
        }
    }

    override fun mouseUpdate(position: Vec2, oldPosition: Vec2) {
        super.mouseUpdate(position, oldPosition)
        if (!isOpen) return

        hoveredEntity = null
        hoveredItem   = null

        val scale   = 0.85f + 0.15f * openProgress
        val cellHs  = cellH * scale
        val cellWs  = cellW * scale
        val gapS    = cellGap * scale
        val h       = panelH * scale
        val startX  = -cellWs / 2f

        val topY   = panelH * scale / 2f - tabH * scale - 60f * scale
        val startY = topY

        when (activeTab) {
            Tab.ENTITIES -> allEntities.forEachIndexed { i, entity ->
                val y = startY - i * (cellHs + gapS) - entityScroll * scale
                if (absInside(0f v y, cellWs v cellHs, position)) hoveredEntity = entity
            }
            Tab.ITEMS -> allItems.forEachIndexed { i, item ->
                val y = startY - i * (cellHs + gapS) - itemScroll * scale
                if (absInside(0f v y, cellWs v cellHs, position)) hoveredItem = item
            }
        }
    }

    private fun absInside(pos: Vec2, size: Vec2, tap: Vec2): Boolean {
        return tap.x >= pos.x - size.x / 2f && tap.x <= pos.x + size.x / 2f &&
                tap.y >= pos.y - size.y / 2f && tap.y <= pos.y + size.y / 2f
    }

    override fun leftPressed(position: Vec2) {
        super.leftPressed(position)
        if (!isOpen) return

        // Клик по табам
        val scale  = 0.85f + 0.15f * openProgress
        val h      = panelH * scale
        val tabY   = h / 2f - tabH * scale / 2f - 12f * scale
        Tab.values().forEachIndexed { i, tab ->
            val tx = -panelW * scale / 2f + 24f * scale + i * (tabW * scale + 8f * scale) + tabW * scale / 2f
            if (absInside(tx v tabY, tabW * scale v tabH * scale, position)) {
                if (tab != targetTab) {
                    targetTab   = tab
                    tabProgress = 0f
                }
                return
            }
        }

        // Клик по кнопкам
        val cellHs = cellH * scale
        val cellWs = cellW * scale
        val gapS   = cellGap * scale
        val topY   = h / 2f - tabH * scale - 60f * scale
        val startY = topY - gapS
        val btnX   = cellWs / 2f - 40f * scale

        when (activeTab) {
            Tab.ENTITIES -> allEntities.forEachIndexed { i, entity ->
                val y = startY - i * (cellHs + gapS) + entityScroll * scale
                if (absInside(btnX v y, 56f * scale v cellHs * 0.65f, position)) {
                    spawnEntity(entity)
                }
            }
            Tab.ITEMS -> allItems.forEachIndexed { i, item ->
                val y = startY - i * (cellHs + gapS) + itemScroll * scale
                if (absInside(btnX v y, 56f * scale v cellHs * 0.65f, position)) {
                    giveItem(item)
                }
            }
        }
    }

    // =====================================================================
    // ДЕЙСТВИЯ
    // =====================================================================

    private fun spawnEntity(entityType: AbstractEntityType) {
        val player = gameController.gameCycle.entityApi.getById(gameController.playerControl.playerId) ?: return
        val facing = player.facing.toFloat()
        val spawnPos = player.position + (facing * 20f v 0f)
        gameController.gameCycle.entityApi.spawnEntity(entityType.tag, spawnPos)
    }

    private fun giveItem(itemType: AbstractItemType) {
        val player = gameController.gameCycle.entityApi.getById(gameController.playerControl.playerId) ?: return
        val item   = gameController.gameCycle.itemsApi.getRegisteredItem(itemType, 1)
        val remaining = player.inventory?.itemContainer?.addItem(item) ?: 1
        if (remaining > 0) {
            gameController.gameCycle.itemsApi.spawnItemEntity(
                gameController.gameCycle.itemsApi.getRegisteredItem(itemType, remaining),
                player.position,
                randomVelocity = true
            )
        }
    }

    // =====================================================================
    // ОТКРЫТИЕ / ЗАКРЫТИЕ
    // =====================================================================

    fun toggle() {
        isOpen = !isOpen
        if (isOpen) {
            entityScroll       = 0f
            itemScroll         = 0f
            targetEntityScroll = 0f
            targetItemScroll   = 0f
        }
        gameController.coreController.windowsManager.destroyWindow(this)
    }

    override fun keyPressed(key: Int) {
        // Ctrl + D
        if (key == KeyCode.F1) toggle()
    }
}