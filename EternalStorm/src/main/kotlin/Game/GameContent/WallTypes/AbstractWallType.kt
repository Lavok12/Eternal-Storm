package la.vok.Game.GameContent.Tiles.System

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.WallData.AbstractWallData
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionContext
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.WallPlaceType
import la.vok.LavokLibrary.Gradient.TransitionGenerator
import la.vok.LavokLibrary.Gradient.TransitionInfo
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.LavokLibrary.Vectors.p
import la.vok.State.AppState
import processing.core.PImage

data class WallRenderConfig(
    val sizeMultiplier: Float = 1.0f,
    val useSquareRender: Boolean = false,
    val renderDelta: Vec2 = 0f v 0f,
    val renderBreakProgress: Boolean = true,
    val useBatchLayer: Boolean = true,
    val AOShadow: Boolean = true,
    val flipX: Boolean = false
)

abstract class AbstractWallType : IBlockType {

    override val tag: String = ""
    override val blockStrength: Int = 0
    override val maxHp: Int = 0
    override val drop: DropEntry = NothingDrop
    override val tags: Set<String> = emptySet()
    override fun isFullBlock(): Boolean = false

    open fun createWallData(x: Int, y: Int, dimension: AbstractDimension): AbstractWallData? = null

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

    open val placeType: WallPlaceType = WallPlaceType.NEAR_WALL_OR_TILE

    open val renderConfig: WallRenderConfig = WallRenderConfig()

    open fun canPlace(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController): Boolean = true

    override open fun render(
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
        val finalW = if (renderConfig.useSquareRender) sizeX * renderConfig.sizeMultiplier else sizeX
        val finalH = if (renderConfig.useSquareRender) sizeX * renderConfig.sizeMultiplier else sizeY

        val centerX = positionX + renderConfig.renderDelta.x * sizeX
        val centerY = positionY + renderConfig.renderDelta.y * sizeY

        lg.setImage(
            gameController.coreController.spriteLoader.getValue(texture),
            centerX, centerY,
            finalW, finalH,
            renderConfig.flipX
        )

        if (renderConfig.renderBreakProgress && !AppState.isBatchRendering) {
            renderBreakProgress(pointX, pointY, lg, centerX, centerY, finalW, finalH, dimension, gameController)
        }
    }

    override fun renderPollution(
        pointX: Int, pointY: Int,
        lg: LGraphics,
        positionX: Float, positionY: Float,
        sizeX: Float, sizeY: Float,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        if (!canBePolluted() || !AppState.enablePollutionRendering) return

        val polluters = HashMap<String, Pair<IBlockType, Int>>()
        val strengths = HashMap<String, Float>()
        val isMutual = HashMap<String, Boolean>()
        val mapApi = dimension.gameCycle.mapApi

        val offsetsX = intArrayOf(0, 1, 0, -1, 1, 1, -1, -1)
        val offsetsY = intArrayOf(1, 0, -1, 0, 1, -1, -1, 1)
        val bits = intArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

        for (i in 0..7) {
            val nx = pointX + offsetsX[i]
            val ny = pointY + offsetsY[i]
            
            val nWall = mapApi.getWallType(dimension, nx, ny)
            if (nWall != null && nWall.tag != this.tag) {
                val strength = nWall.getPollutionStrength(this, dimension, nx, ny)
                if (strength > 0f) {
                    val current = polluters[nWall.tag] ?: Pair(nWall, 0)
                    polluters[nWall.tag] = Pair(nWall, current.second or bits[i])
                    strengths[nWall.tag] = kotlin.math.max(strengths[nWall.tag] ?: 0f, strength)
                    val mutual = getPollutionStrength(nWall, dimension, pointX, pointY) > 0f
                    isMutual[nWall.tag] = (isMutual[nWall.tag] ?: false) || mutual
                }
            }
        }

        if (polluters.isEmpty()) return

        val finalW = if (renderConfig.useSquareRender) sizeX * renderConfig.sizeMultiplier else sizeX
        val finalH = if (renderConfig.useSquareRender) sizeX * renderConfig.sizeMultiplier else sizeY

        val centerX = positionX + renderConfig.renderDelta.x * sizeX
        val centerY = positionY + renderConfig.renderDelta.y * sizeY

        for ((tag, data) in polluters) {
            val block = data.first
            val mask = data.second
            val strength = strengths[tag] ?: 1f
            val mutual = isMutual[tag] ?: false
            val cornerStrength = if (mutual) strength * 0.5f else strength
            val texture = block.getPollutionTexture(dimension, pointX, pointY, pointX, pointY, gameController)
            if (texture != null) {
                val transitionInfo = TransitionInfo(texture, mask, strength, cornerStrength)
                val generated = TransitionGenerator.generate(transitionInfo, AppState.main)
                lg.setImage(generated, centerX, centerY, finalW, finalH, renderConfig.flipX)
            }
        }
    }

    open fun breakProgress(pointX: Int, pointY: Int, dimension: AbstractDimension, gameController: GameController) : Float {
        if (maxHp <= 0) return 0f
        return (1f - gameController.gameCycle.mapApi.getWallHp(dimension, pointX, pointY) / maxHp.toFloat()).coerceIn(0f, 1f)
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

        lg.setTint(130f)
        lg.setImage(
            texture,
            positionX, positionY,
            sizeX, sizeY
        )
        lg.noTint()
    }

    open fun place(x: Int, y: Int, item: Item, mapController: MapController) {}

    open fun damage(x: Int, y: Int, damage: Int, dimension: AbstractDimension, mapController: MapController) {
        mapController.dimension.gameCycle.particlesApi.buildWall(dimension, this).atBlock(x, y).count(3).randomSpeed(1f).spawn()
    }

    open fun onMined(x: Int, y: Int, mineData: MineData, dimension: AbstractDimension, mapController: MapController) {
        val pos = dimension.gameCycle.mapApi.getBlockPos(x, y)
        dimension.gameCycle.itemsApi.spawnDropTable(dimension, drop, Vec2(pos.x + 0.5f, pos.y + 0.5f), true)
    }

    open fun onRemoved(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController, reason: Any? = null) {}

    open fun onUpdate(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController) {
        dimension.mapSystem.getWallData(x, y)?.onUpdate()
    }
}