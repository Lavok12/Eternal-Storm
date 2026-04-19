package la.vok.Game.GameSystems.RenderSystems.EffectLayers

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.Layers.EffectLayer
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.LavokLibrary.KotlinPlus.forEachInArea
import la.vok.LavokLibrary.Vectors.LPoint

class AOTiles(var gameRender: GameRender, point: LPoint, mp: Float = 0.7f) : EffectLayer(point, mp) {

    lateinit var camera: Camera

    lateinit var dim: AbstractDimension

    override fun draw() {
        val lg = this.lg ?: return
        val gameController = gameRender.gameController

        if (gameController.playerControl.getPlayerEntity() != null) {
            dim = gameController.playerDimension!!
        }
        lg.noStroke()
        lg.pg.clear()

        val mapApi = gameController.gameCycle.mapApi
        val mapSystem = dim.mapSystem

        val p1 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameLeftBottom))
        val p2 = mapApi.getPointFromPos(camera.toWorldPos(gameController.wGamePanel!!.frameRightTop))

        val blockScaleX = camera.useCameraSizeX(1f)
        val blockScaleY = camera.useCameraSizeY(1f)

        forEachInArea(p1, p2, 1) { ix, iy ->
            val tileType = gameController.gameCycle.mapApi.getTileType(dim, ix, iy) ?: return@forEachInArea

            if (!tileType.tags.contains(BlockTags.NO_SHADOW)) {
                val cx = camera.useCameraPosX(ix.toFloat())
                val cy = camera.useCameraPosY(iy.toFloat())

                lg.fill(0f)
                lg.setBlock(cx, cy, blockScaleX + 1f, blockScaleY + 1f)
            }
        }

        val shader = gameController.coreController.shaderLoader.getValue("blur.glsl")
        val pg = lg.pg
        shader.set("dis", pg.width.toFloat(), pg.height.toFloat())
        shader.set("power", pg.width.toFloat() / 400.0f)
        pg.filter(shader)
        shader.set("power", pg.width.toFloat() / 800.0f)
        pg.filter(shader)
        lg.beginDraw()
    }
}