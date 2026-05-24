package la.vok.Game.GameContent.VfxObjects.SinWave

import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import kotlin.math.atan2

class SinWaveVFXSparkParticle(gameCycle: GameCycle) : SinWaveVFXColorBlended(gameCycle) {
    var friction = 0.96f // Выше трение = быстрее останавливаются

    init {
        lifetime = 30L + (Math.random() * 10).toLong() // Короче
    }

    override fun physicUpdate() {
        super.physicUpdate()
        speed = speed * friction
    }

    override fun renderParticle(lg: LGraphics, pos: Vec2, size: Vec2) {
        val texture = gameController.coreController.spriteLoader.getValue("glow.png")

        val velocityMagnitude = speed.length()
        val stretchFactor = 1f + velocityMagnitude * 10f
        val sparkSize = Vec2(size.x * stretchFactor)

        lg.setImage(texture, pos, sparkSize * 2f)
    }
}