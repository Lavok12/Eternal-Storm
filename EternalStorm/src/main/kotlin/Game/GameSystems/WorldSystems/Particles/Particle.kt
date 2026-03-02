package la.vok.Game.GameSystems.WorldSystems.Particles

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import processing.core.PImage

open class Particle(var gameCycle: GameCycle, var pos: Vec2, var size: Vec2, var speed: Vec2) {
    open var rotate = 0f
    open var rotateSpeed = 0f
    open var lifetime = 120f
    open var isDelete = false
    open var G = -0.003f

    open fun physicUpdate() {
        pos = pos + speed
        rotate += rotateSpeed
        speed.y += G
        lifetime--
        if (lifetime < 0) {
            delete()
        }
    }
    open fun render(lg: LGraphics, camera: Camera) {

    }

    open fun delete() {
        isDelete = true
    }
}