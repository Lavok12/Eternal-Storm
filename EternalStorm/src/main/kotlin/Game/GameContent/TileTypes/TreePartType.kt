package la.vok.Game.GameContent.TileTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Entities.Entities.Special.FallingTreeSegmentEntity
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import processing.core.PImage
import kotlin.math.*

class TreePartType() : AbstractTileType() {
    override var collisionType: CollisionType = CollisionType.NONE
    override val tag: String = TilesList.tree_part_block
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = AppState.res("tree_part_block_1.png")
    override val drop: DropEntry = NothingDrop
    override val tags = setOf(BlockTags.WOOD, BlockTags.SOLID)
    override val renderConfig = TileRenderConfig(AOShadow = false)

    override fun render(
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
        val mapApi = gameController.gameCycle.mapApi
        val tagType = mapApi.getRegisteredTileType(tag)

        when {
            mapApi.getTileType(dimension, pointX, pointY - 1) != tagType ->
                renderBottom(lg, positionX, positionY, sizeX, sizeY, gameController)

            mapApi.getTileType(dimension, pointX, pointY + 1) != tagType ->
                renderTop(lg, positionX, positionY, sizeX, sizeY, gameController)

            else ->
                renderMiddle(pointX, pointY, lg, positionX, positionY, sizeX, sizeY, gameController)
        }
    }

    fun renderBottom(
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        gameController: GameController
    ) {
        val texture = gameController.coreController.spriteLoader.getValue(AppState.res("tree_part_block_1.png"))
        lg.setImage(texture, positionX, positionY, sizeX, sizeY, renderConfig.flipX)
    }

    fun renderMiddle(
        pointX: Int,
        pointY: Int,
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        gameController: GameController
    ) {
        val texture = gameController.coreController.spriteLoader.getValue(AppState.res("tree_part_block_2.png"))
        lg.setImage(texture, positionX, positionY, sizeX, sizeY, renderConfig.flipX)

        val subtype = getMiddleSubtype(pointX, pointY)
        if (subtype == 0) return

        when (subtype) {
            1 -> {
                val t = gameController.coreController.spriteLoader.getValue(AppState.res("tree_part_block_2_-1.png"))
                lg.setImage(t, positionX, positionY, sizeX * 5f, sizeY * 5f, renderConfig.flipX)
            }
            2 -> {
                val t = gameController.coreController.spriteLoader.getValue(AppState.res("tree_part_block_2_+1.png"))
                lg.setImage(t, positionX, positionY, sizeX * 5f, sizeY * 5f, renderConfig.flipX)
            }
            3 -> {
                var t = gameController.coreController.spriteLoader.getValue("tree_part_block_2_-1.png")
                lg.setImage(t, positionX, positionY, sizeX * 5f, sizeY * 5f, renderConfig.flipX)

                t = gameController.coreController.spriteLoader.getValue("tree_part_block_2_+1.png")
                lg.setImage(t, positionX, positionY, sizeX * 5f, sizeY * 5f, renderConfig.flipX)
            }
        }
    }

    fun getMiddleSubtype(x: Int, y: Int): Int {
        if ((x + y) % 2 == 0) return 0

        val hash = ((x * 73428767) xor (y * 912931)).absoluteValue % 100

        return when {
            hash < 60 -> 0
            hash < 75 -> 1
            hash < 90 -> 2
            else -> 3
        }
    }

    override fun onMined(x: Int, y: Int, mineData: MineData, dimension: AbstractDimension, mapController: MapController) {
        val mapApi = dimension.gameCycle.mapApi
        val spriteLoader = dimension.gameCycle.gameController.coreController.spriteLoader

        val tileYList = mutableListOf<Int>()
        var checkY = y
        while (mapApi.getTileType(dimension, x, checkY)?.tag == tag) {
            tileYList.add(checkY)
            checkY++
        }
        if (tileYList.isEmpty()) return
        val totalHeight = tileYList.size

        data class SegmentTextures(
            val main: PImage,
            val bg: List<PImage>,
            val topLeaf: PImage?
        )

        val segmentTextures = tileYList.mapIndexed { index, ty ->
            val isBottom = mapApi.getTileType(dimension, x, ty - 1)?.tag != tag
            val isTop    = mapApi.getTileType(dimension, x, ty + 1)?.tag != tag

            val mainTex = when {
                isBottom -> spriteLoader.getValue(AppState.res("tree_part_block_1.png"))
                isTop    -> spriteLoader.getValue(AppState.res("tree_part_block_3.png"))
                else     -> spriteLoader.getValue(AppState.res("tree_part_block_2.png"))
            }

            val topLeaf = if (isTop) spriteLoader.getValue(AppState.res("tree_part_block_3_1.png")) else null

            val bgList = mutableListOf<PImage>()
            if (!isBottom && !isTop) {
                when (getMiddleSubtype(x, ty)) {
                    1 -> bgList.add(spriteLoader.getValue("tree_part_block_2_-1.png"))
                    2 -> bgList.add(spriteLoader.getValue("tree_part_block_2_+1.png"))
                    3 -> {
                        bgList.add(spriteLoader.getValue("tree_part_block_2_-1.png"))
                        bgList.add(spriteLoader.getValue("tree_part_block_2_+1.png"))
                    }
                }
            }

            SegmentTextures(mainTex, bgList, topLeaf)
        }

        tileYList.forEach { ty -> mapApi.deleteTile(dimension, x, ty) }

        val fallRight = x % 2 == 0
        val sharedPivot = (x.toFloat()) v (y.toFloat() - mapApi.getBlockSize().y * 0.5f)
        val siblings = mutableListOf<FallingTreeSegmentEntity>()

        segmentTextures.forEachIndexed { index, texData ->
            val entity = FallingTreeSegmentEntity(
                gameCycle = dimension.gameCycle,
                texture = texData.main,
                bgTextures = texData.bg,
                topLeafTexture = texData.topLeaf,
                segmentIndex = index,
                sharedPivot = sharedPivot,
                fallRight = fallRight,
                treeHeight = totalHeight,
                siblings = siblings
            )
            siblings.add(entity)
        }

        siblings.forEach { entity ->
            dimension.gameCycle.entityApi.spawnEntity(dimension, entity, sharedPivot.copy())
        }
    }


    fun renderTop(
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        gameController: GameController
    ) {
        var texture = gameController.coreController.spriteLoader.getValue(AppState.res("tree_part_block_3_1.png"))
        lg.setImage(texture, positionX, positionY, sizeX * 6f, sizeY * 6f, renderConfig.flipX)
        texture = gameController.coreController.spriteLoader.getValue(AppState.res("tree_part_block_3.png"))
        lg.setImage(texture, positionX, positionY, sizeX, sizeY, renderConfig.flipX)
    }
}