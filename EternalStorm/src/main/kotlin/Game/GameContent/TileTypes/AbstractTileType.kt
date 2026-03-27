package la.vok.Game.GameContent.Tiles.System

import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.TilePlaceType
import la.vok.LavokLibrary.Gradient.ShadowInfo
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p
import processing.core.PImage

abstract class AbstractTileType : IBlockType {
    open var collisionType = CollisionType.FULL
    override val tag: String = ""
    override val blockStrength: Int = 0
    override val maxHp: Int = 0
    override val texture: String = ""
    override val drop: DropEntry = NothingDrop

    open val placeType: TilePlaceType = TilePlaceType.NEAR_TILE_OR_ON_WALL

    open fun canPlace(context: TileContext): Boolean = true

    open fun render(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        lg.setImage(
            gameController.coreController.spriteLoader.getValue(texture),
            position,
            size
        )
        renderBreakProgress(tileContext, lg, position, size, gameController)
    }

    open fun breakProgress(tileContext: TileContext) : Float {
        return 1-tileContext.hp/maxHp.toFloat()
    }
    open fun getBreakProgressTexture(progress: Float, gameController: GameController) : PImage? {
        if (progress == 0f) {return null}
        if (progress < 0.33f) {return gameController.coreController.spriteLoader.getValue("t1.png")}
        if (progress < 0.66f) {return gameController.coreController.spriteLoader.getValue("t2.png")}
        if (progress < 1f) {return gameController.coreController.spriteLoader.getValue("t3.png")}
        return null
    }
    open fun renderBreakProgress(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        var progress = breakProgress(tileContext)

        var texture = getBreakProgressTexture(progress, gameController)
        if (texture != null) {
            lg.setImage(
                ShadowInfo(texture, 120 p 120, 10, 2, true).generate(),
                position,
                size*1.2f
            )
            lg.setTint(130f)
            lg.setImage(
                texture,
                position,
                size
            )
            lg.noTint()
        }
    }

    open fun renderHighlight(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameRender: GameRender
    ) {
        lg.stroke(0f)
        lg.strokeWeight(3f)
        lg.fill(0f, 0f)
        lg.setBlock(position, size-3f)
        lg.noStroke()
    }

    open fun place(x: Int, y: Int, item: Item, mapController: MapController) {}

    open fun spawnTileParticle(x: Int, y: Int, tileContext: TileContext, mapController: MapController, speed: Vec2 = Vec2.ZERO) {
        mapController.gameCycle.particlesApi.spawnTileParticleRandom(this, mapController.gameCycle.mapApi.getBlockPos(x, y), 1f)
    }

    open fun damage(x: Int, y: Int, damage: Int, tileContext: TileContext, mapController: MapController) {
        mapController.gameCycle.particlesApi.spawnTileParticles(this, mapController.gameCycle.mapApi.getBlockPos(x, y), 5)
    }

    open fun onMined(x: Int, y: Int, mineData: MineData, tileContext: TileContext, mapController: MapController) {
        val pos = mapController.mapApi.getBlockPos(x p y)
        mapController.gameCycle.itemsApi.spawnDropTable(drop, pos, true)
        mapController.gameCycle.particlesApi.spawnTileParticles(this, pos, 9)
    }

    open fun onRemoved(
        x: Int,
        y: Int,
        tileContext: TileContext,
        reason: Any? = null
    ) {
    }
}