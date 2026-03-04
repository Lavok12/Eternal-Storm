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

open class TileParticle(gameCycle: GameCycle, var tile: AbstractTileType, pos: Vec2, speed: Vec2, size: Vec2 = 0.3 v 0.3f) : Particle(gameCycle, pos, size, speed){
    override var rotate = AppState.main.random(3.1415927f * 2f)
    override var rotateSpeed = AppState.main.random(-0.01f, 0.01f)

    override var lifetime = 50f
    override var isDelete = false

    var pImage: PImage = ParticleSplitInfo(
        gameCycle.gameController.coreController.spriteLoader.getValue(tile.texture),
        5, 5,
        20 p 20,
        true
        ).generate().random()

    override fun physicUpdate() {
        super.physicUpdate()
        size.x -= 0.005f
        size.y -= 0.005f
    }

    override fun render(lg: LGraphics, camera: Camera) {
        lg.setTint(0f, 100f)
        lg.setRotateImage(AppState.coreController.spriteLoader.getValue("BoxShadow.png"), camera.useCamera(pos), camera.useCameraSize(size*2f), rotate)
        lg.noTint()
        lg.setRotateImage(pImage, camera.useCamera(pos), camera.useCameraSize(size), rotate)
    }
}