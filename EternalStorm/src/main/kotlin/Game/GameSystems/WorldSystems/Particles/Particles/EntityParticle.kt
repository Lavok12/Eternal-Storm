package la.vok.Game.GameSystems.WorldSystems.Particles.Particles

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Particles.Particle
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Particles.ParticleSplitInfo
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import processing.core.PImage

open class EntityParticle(gameCycle: GameCycle, image: PImage, pos: Vec2, speed: Vec2, size: Vec2 = 0.28 v 0.28f) : Particle(gameCycle, pos, size, speed){
    override var rotate = AppState.main.random(3.1415927f * 2f)
    override var rotateSpeed = AppState.main.random(-0.01f, 0.01f)

    override var lifetime = 100f
    override var isDelete = false

    init {
        G = -0.002f
    }
    var pImage: PImage = ParticleSplitInfo(
        image,
        2, 2,
        40 p 40,
        true
    ).generate().random()

    override fun physicUpdate() {
        super.physicUpdate()
        size.x -= 0.002f
        size.y -= 0.002f
    }

    override fun render(lg: LGraphics, camera: Camera) {
        lg.setTint(255f, lifetime*2f)
        lg.setRotateImage(pImage, camera.useCamera(pos), camera.useCameraSize(size), rotate)
        lg.noTint()
    }
}