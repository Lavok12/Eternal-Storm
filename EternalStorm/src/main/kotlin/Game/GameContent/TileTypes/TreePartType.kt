package la.vok.Game.GameContent.TileTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Entities.Entities.Special.FallingTreeSegmentEntity
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameSystems.WorldSystems.Particles.Particle
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState
import processing.core.PImage
import kotlin.math.*

class TreePartType() : AbstractTileType() {
    override var collisionType: CollisionType = CollisionType.NONE
    override val tag: String = TilesList.tree_part_block
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = "tree_part_block_1.png"
    override val drop: DropEntry = NothingDrop

    override fun render(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        val mapApi = gameController.gameCycle.mapApi
        val tagType = mapApi.getRegisteredTileType(tag)

        when {
            mapApi.getTileType(tileContext.dimension!!, tileContext.positionX, tileContext.positionY - 1) != tagType ->
                renderBottom(tileContext, lg, position, size, gameController)

            mapApi.getTileType(tileContext.dimension!!, tileContext.positionX, tileContext.positionY + 1) != tagType ->
                renderTop(tileContext, lg, position, size, gameController)

            else ->
                renderMiddle(tileContext, lg, position, size, gameController)
        }
    }

    fun renderBottom(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        val texture = gameController.coreController.spriteLoader.getValue("tree_part_block_1.png")
        lg.setImage(texture, position, size)
    }

    fun renderMiddle(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        val x = tileContext.positionX
        val y = tileContext.positionY

        val texture = gameController.coreController.spriteLoader.getValue("tree_part_block_2.png")
        lg.setImage(texture, position, size)

        val subtype = getMiddleSubtype(x, y)
        if (subtype == 0) return

        when (subtype) {
            1 -> {
                val t = gameController.coreController.spriteLoader.getValue("tree_part_block_2_-1.png")
                lg.setImage(t, position, size * 5f)
            }
            2 -> {
                val t = gameController.coreController.spriteLoader.getValue("tree_part_block_2_+1.png")
                lg.setImage(t, position, size * 5f)
            }
            3 -> {
                var t = gameController.coreController.spriteLoader.getValue("tree_part_block_2_-1.png")
                lg.setImage(t, position, size * 5f)

                t = gameController.coreController.spriteLoader.getValue("tree_part_block_2_+1.png")
                lg.setImage(t, position, size * 5f)
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

    override fun onMined(x: Int, y: Int, mineData: MineData, tileContext: TileContext, mapController: MapController) {
        val mapApi = mapController.dimension.gameCycle.mapApi
        val spriteLoader = mapController.dimension.gameCycle.gameController.coreController.spriteLoader

        val tileYList = mutableListOf<Int>()
        var checkY = y
        while (mapApi.getTileType(mapController.dimension, x, checkY)?.tag == tag) {
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
            val isBottom = mapApi.getTileType(mapController.dimension, x, ty - 1)?.tag != tag
            val isTop    = mapApi.getTileType(mapController.dimension, x, ty + 1)?.tag != tag

            val mainTex = when {
                isBottom -> spriteLoader.getValue("tree_part_block_1.png")
                isTop    -> spriteLoader.getValue("tree_part_block_3.png")
                else     -> spriteLoader.getValue("tree_part_block_2.png")
            }

            val topLeaf = if (isTop) spriteLoader.getValue("tree_part_block_3_1.png") else null

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

        tileYList.forEach { ty -> mapApi.deleteTile(mapController.dimension, x, ty) }

        val fallRight = x % 2 == 0
        val sharedPivot = Vec2(x.toFloat(), y.toFloat() - mapApi.getBlockSize().y * 0.5f)
        val siblings = mutableListOf<FallingTreeSegmentEntity>()

        segmentTextures.forEachIndexed { index, texData ->
            val entity = FallingTreeSegmentEntity(
                gameCycle = mapController.dimension.gameCycle,
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
            mapController.dimension.gameCycle.entityApi.spawnEntity(mapController.dimension, entity, sharedPivot.copy())
        }
    }


    fun renderTop(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        var texture = gameController.coreController.spriteLoader.getValue("tree_part_block_3_1.png")
        lg.setImage(texture, position, size*6f)
        texture = gameController.coreController.spriteLoader.getValue("tree_part_block_3.png")
        lg.setImage(texture, position, size)
    }
}