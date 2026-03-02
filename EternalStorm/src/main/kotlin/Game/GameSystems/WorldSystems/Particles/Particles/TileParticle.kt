package la.vok.Game.GameSystems.WorldSystems.Particles.Particles

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Particles.Particle
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Particles.ParticleSplitInfo
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import processing.core.PImage

open class TileParticle(gameCycle: GameCycle, var tile: AbstractTileType, pos: Vec2, speed: Vec2, size: Vec2 = 0.28 v 0.28f) : Particle(gameCycle, pos, size, speed){
    override var rotate = AppState.main.random(3.1415927f * 2f)
    override var rotateSpeed = AppState.main.random(-0.01f, 0.01f)

    override var lifetime = 45f
    override var isDelete = false

    var pImage: PImage = ParticleSplitInfo(
        gameCycle.gameController.coreController.spriteLoader.getValue(tile.texture),
        5, 5,
        AppState.main.random(5f).toInt() p AppState.main.random(5f).toInt(),
        true
        ).generate().random()

    override fun physicUpdate() {
        super.physicUpdate()
        size.x -= 0.002f
        size.y -= 0.002f
    }

    override fun render(lg: LGraphics, camera: Camera) {
        lg.setTint(0f, 100f)
        lg.setRotateImage(AppState.coreController.spriteLoader.getValue("BoxShadow.png"), camera.useCamera(pos), camera.useCameraSize(size*2f), rotate)
        lg.noTint()
        lg.setRotateImage(pImage, camera.useCamera(pos), camera.useCameraSize(size), rotate)
    }
}