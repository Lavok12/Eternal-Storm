package la.vok.Game.GameSystems.EffectLayers

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.Layers.EffectLayer
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Tiles.System.TileContext
import la.vok.Game.GameSystems.WorldSystems.Map.WallContext
import la.vok.LavokLibrary.KotlinPlus.forEachInArea
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.ContentList.DimensionsList
import processing.core.PConstants
import kotlin.plus
import kotlin.times

class AOTiles(var gameRender: GameRender, point: LPoint, mp: Float = 0.7f) : EffectLayer(point, mp) {

    lateinit var camera: Camera
    var dimension: AbstractDimension? = null

    override fun draw() {
        lg?.noStroke()
        lg?.pg?.clear()
        val dim = gameRender.gameController.playerControl.getPlayerEntity()!!.dimension!!
        val mapApi = gameRender.gameController.gameCycle.mapApi
        val mapSystem = dim.mapSystem

        var p1 = mapApi.getPointFromPos(camera.toWorldPos(gameRender.gameController.wGamePanel!!.frameLeftBottom))
        var p2 = mapApi.getPointFromPos(camera.toWorldPos(gameRender.gameController.wGamePanel!!.frameRightTop))


        var tileContext = TileContext()
        forEachInArea(p1, p2, 1) { ix, iy ->
            val mapTile = mapSystem.getTileType(ix, iy) ?: return@forEachInArea

            if (mapSystem.containsTile(ix, iy)) {
                tileContext.hp = mapApi.getTileHp(dim, ix, iy)
                tileContext.positionX = ix
                tileContext.positionY = iy
                tileContext.tileType = mapTile

                var tilePos = mapApi.getBlockPos(dim, ix p iy)
                var tileSize = mapApi.getBlockSize()

                if (mapTile != mapApi.getRegisteredTileType(TilesList.tree_part_block)) {
                    lg?.fill(0f)
                    lg?.setBlock(camera.useCamera(tilePos), camera.useCameraSize(tileSize) * 1f + (1 v 1))
                }
            }
        }

        var shader = gameRender.gameController.coreController.shaderLoader.getValue("blur.glsl")
        shader.set("dis", lg!!.pg.width.toFloat(), lg!!.pg.height.toFloat())
        shader.set("power", lg!!.pg.width.toFloat()/400.0f)
        lg?.pg?.filter(shader)
        shader.set("power", lg!!.pg.width.toFloat()/800.0f)
        lg?.pg?.filter(shader)
        lg?.beginDraw()
    }
}
