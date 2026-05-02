package la.vok.Game.GameContent.Tiles.System

import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionContext
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.TilePlaceType
import la.vok.LavokLibrary.Gradient.ShadowInfo
import la.vok.Game.GameContent.TileData.AbstractTileData
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import processing.core.PImage

data class TileRenderConfig(
    var sizeMultiplier: Float = 1.0f,
    var useSquareRender: Boolean = false,
    var renderDelta: Vec2 = 0f v 0f,
    var renderBreakProgress: Boolean = true,
    var useBatchLayer: Boolean = true,
    var renderWallsBehind: Boolean = false,
    var AOShadow: Boolean = true,
    var flipX: Boolean = false
)

abstract class AbstractTileType : IBlockType {
    open var collisionType = CollisionType.FULL
    override val tag: String = ""
    override val blockStrength: Int = 0
    override val maxHp: Int = 0
    override val texture: String = ""
    override val drop: DropEntry = NothingDrop
    override val tags: Set<String> = emptySet()

    open val canBeReplaced: Boolean = false

    open fun hasTag(tag: String): Boolean = tag in tags
    
    val interactionReactions = mutableMapOf<BlockInteractionType, MutableList<(BlockInteractionContext) -> Unit>>()

    fun addInteractionReaction(type: BlockInteractionType, reaction: (BlockInteractionContext) -> Unit) {
        interactionReactions.getOrPut(type) { mutableListOf() }.add(reaction)
    }

    override fun onInteract(type: BlockInteractionType, context: BlockInteractionContext): Boolean {
        val list = interactionReactions[type] ?: return false
        if (list.isEmpty()) return false
        list.forEach { it.invoke(context) }
        return true
    }

    open fun onPositionChanged(oldX: Int, oldY: Int, newX: Int, newY: Int, dimension: AbstractDimension) {}

    open val width: Int = 1
    open val height: Int = 1
    open val isDummy: Boolean = false
    open val masterOffset: LPoint = 0 p 0
    open val placeOffset: LPoint = 0 p 0

    open fun createTileData(x: Int, y: Int, dimension: AbstractDimension): AbstractTileData? = null

    open val renderConfig: TileRenderConfig = TileRenderConfig()

    open val placeType: TilePlaceType = TilePlaceType.NEAR_TILE_OR_ON_WALL
    open val breakIfInvalid: Boolean = false

    open fun canPlace(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController): Boolean = true

    open fun render(
        pointX: Int,
        pointY: Int,
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        if (isDummy) return

        val baseW = sizeX * width
        val baseH = sizeY * height
        
        var finalW = baseW * renderConfig.sizeMultiplier
        var finalH = baseH * renderConfig.sizeMultiplier
        
        if (renderConfig.useSquareRender) {
            val s = kotlin.math.min(finalW, finalH)
            finalW = s
            finalH = s
        }
        
        // positionX/Y are passed as the true geometric center of the tile area
        val centerX = positionX + renderConfig.renderDelta.x * sizeX
        val centerY = positionY + renderConfig.renderDelta.y * sizeY

        lg.setImage(
            gameController.coreController.spriteLoader.getValue(texture),
            centerX,
            centerY,
            finalW,
            finalH,
            renderConfig.flipX
        )
        
        if (renderConfig.renderBreakProgress && !AppState.isBatchRendering) {
            renderBreakProgress(pointX, pointY, lg, centerX, centerY, finalW, finalH, dimension, gameController)
        }
    }

    open fun breakProgress(pointX: Int, pointY: Int, dimension: AbstractDimension, gameController: GameController) : Float {
        if (maxHp <= 0) return 0f
        return (1f - gameController.gameCycle.mapApi.getTileHp(dimension, pointX, pointY) / maxHp.toFloat()).coerceIn(0f, 1f)
    }

    open fun getBreakProgressTexture(progress: Float, gameController: GameController) : PImage? {
        if (progress <= 0f) return null
        if (progress < 0.33f) return gameController.coreController.spriteLoader.getValue(AppState.res("t1.png"))
        if (progress < 0.66f) return gameController.coreController.spriteLoader.getValue(AppState.res("t2.png"))
        if (progress < 1f) return gameController.coreController.spriteLoader.getValue(AppState.res("t3.png"))
        return null
    }

    open fun renderBreakProgress(
        pointX: Int,
        pointY: Int,
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        val progress = breakProgress(pointX, pointY, dimension, gameController)
        val texture = getBreakProgressTexture(progress, gameController) ?: return

        lg.setImage(
            ShadowInfo(texture, 120 p 120, 10, 2, true).generate(),
            positionX, positionY,
            sizeX * 1.2f, sizeY * 1.2f
        )
        lg.setTint(130f)
        lg.setImage(
            texture,
            positionX, positionY,
            sizeX, sizeY
        )
        lg.noTint()
    }

    open fun renderHighlight(
        pointX: Int,
        pointY: Int,
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        gameRender: GameRender
    ) {
        lg.stroke(0f)
        lg.strokeWeight(3f)
        lg.fill(0f, 0f)
        lg.setBlock(positionX, positionY, sizeX-3f, sizeY-3f)
        lg.noStroke()

    }

    open fun place(x: Int, y: Int, item: Item, mapController: MapController) {}

    open fun spawnTileParticle(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController, speed: Vec2 = Vec2.ZERO) {
        mapController.dimension.gameCycle.particlesApi.buildTile(dimension, this).atBlock(x, y).speed(speed).randomSpeed(1f).spawn()
    }

    open fun damage(x: Int, y: Int, damage: Int, dimension: AbstractDimension, mapController: MapController) {
        mapController.dimension.gameCycle.particlesApi.buildTile(dimension, this).atBlock(x, y).count(5).randomSpeed(1f).spawn()
    }

    open fun mine(x: Int, y: Int, mineData: MineData, dimension: AbstractDimension, mapController: MapController) {

    }

    open fun onMined(x: Int, y: Int, mineData: MineData, dimension: AbstractDimension, mapController: MapController) {
        val pos = dimension.gameCycle.mapApi.getBlockPos(x, y)
        val spawnPos = Vec2(pos.x + 0.5f, pos.y + 0.5f) - (placeOffset.toVec())
        dimension.gameCycle.itemsApi.spawnDropTable(dimension, drop, spawnPos, true)
        
        // Area particles
        for (dx in 0 until width) {
            for (dy in 0 until height) {
                dimension.gameCycle.particlesApi.buildTile(dimension, this)
                    .atBlock(x + dx, y + dy)
                    .count(3)
                    .randomSpeed(1f)
                    .spawn()
            }
        }
    }

    open fun onRemoved(
        x: Int,
        y: Int,
        dimension: AbstractDimension,
        mapController: MapController,
        reason: Any? = null
    ) {
    }

    open fun onUpdate(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController) {
        if (isDummy) {
            val masterX = x + masterOffset.x
            val masterY = y + masterOffset.y
            dimension.mapSystem.updateBlock(masterX, masterY)
            return
        }
        
        if (breakIfInvalid) {
            if (!dimension.gameCycle.mapApi.checkStability(dimension, this, x, y)) {
                dimension.gameCycle.mapApi.mineTile(dimension, x, y, MineData(
                    value = maxHp + 1000,
                    power = 1000000,
                    sourceId = null,
                    instrument = null,
                    item = null
                ))
                return
            }
        }
        dimension.mapSystem.getTileData(x, y)?.onUpdate()
    }
}