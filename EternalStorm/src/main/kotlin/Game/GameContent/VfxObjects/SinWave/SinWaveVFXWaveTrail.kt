package la.vok.Game.GameContent.VfxObjects.SinWave

import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import kotlin.math.sin

class SinWaveVFXWaveTrail(gameCycle: GameCycle) : SinWaveVFXColorBlended(gameCycle) {
    var initialSize = Vec2.ZERO
    var waveFrequency = 0.2f
    var waveAmplitude = 0.2f
    var perpendicularDir = Vec2.ZERO

    init {
        lifetime = 20L // Короче для оптимизации
    }

    override fun physicUpdate() {
        super.physicUpdate()
        val wave = sin(physicTicks * waveFrequency) * waveAmplitude
        position = position + perpendicularDir * wave

        size = initialSize
    }

    override fun renderParticle(lg: LGraphics, pos: Vec2, size: Vec2) {
        val texture = gameController.coreController.spriteLoader.getValue("glow.png")
        lg.setImage(texture, pos, size * 2f)
    }
}