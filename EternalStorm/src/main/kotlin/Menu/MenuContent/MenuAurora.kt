package la.vok.Menu.MenuContent

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow
import la.vok.LavokLibrary.Gradient.GradientInfo
import la.vok.LavokLibrary.Vectors.LColor
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState
import processing.core.PImage
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.abs
import kotlin.math.pow

class MenuAurora(
    private val parent: AbstractWindow
) {
    private data class AuroraWave(
        var centerX: Float,
        var baseY: Float,
        var horizontalSpread: Float,
        var amplitude: Float,
        var frequency: Float,
        var phase: Float,
        var phaseSpeed: Float,
        var height: Float,
        var alpha: Float,
        var targetAlpha: Float,
        var fadeInProgress: Float,
        var lifeTime: Float,
        var maxLifeTime: Float,
        var gradient: PImage,
        var depthFactor: Float,
        var noiseOffset: Float,
        var verticalDrift: Float,
        var horizontalDrift: Float,
        var twistAmount: Float,
        var pulseSpeed: Float,
        var pulseAmount: Float
    )

    private val waves = mutableListOf<AuroraWave>()
    private val waveCount = 3
    private var time = 0f

    private val auroraColorSchemes = listOf(
        Pair(LColor(50f, 255f, 100f, 100f), LColor(120f, 20f, 200f, 0f)),
        Pair(LColor(80f, 255f, 150f, 100f), LColor(30f, 50f, 200f, 0f)),
        Pair(LColor(255f, 80f, 200f, 100f), LColor(150f, 30f, 150f, 0f)),
        Pair(LColor(200f, 50f, 255f, 100f), LColor(80f, 200f, 100f, 0f)),
        Pair(LColor(80f, 255f, 220f, 180f), LColor(30f, 100f, 200f, 0f)),
        Pair(LColor(255f, 100f, 120f, 100f), LColor(100f, 40f, 50f, 0f)),
        Pair(LColor(100f, 200f, 255f, 100f), LColor(200f, 80f, 100f, 0f))
    )

    init {
        repeat(waveCount) { i ->
            waves.add(generateWave(i))
        }
    }

    private fun generateGradient(): PImage {
        val scheme = auroraColorSchemes.random()
        return GradientInfo(scheme.first, scheme.second, LPoint(0, 99), LPoint(0, 0), LPoint(1, 100)).generate()
    }

    private fun generateWave(index: Int): AuroraWave {
        val normalizedDepth = index.toFloat() / waveCount.toFloat()
        val baseY = -parent.logicalSize.y * 0.3f + normalizedDepth * parent.logicalSize.y * 0.2f
        val depthFactor = 1f - normalizedDepth * 0.5f

        // Добавляем случайную задержку старта для каждой волны
        val startDelay = index * 5f // Каждая волна стартует с задержкой 5 секунд

        return AuroraWave(
            centerX = 0f, // Центр экрана
            baseY = baseY,
            horizontalSpread = AppState.main.random(800f, 1500f) * depthFactor,
            amplitude = AppState.main.random(50f, 100f) * depthFactor,
            frequency = AppState.main.random(0.003f, 0.008f),
            phase = AppState.main.random(0f, 6.28f),
            phaseSpeed = AppState.main.random(0.2f, 0.6f),
            height = AppState.main.random(150f, 300f) * depthFactor,
            alpha = 0f,
            targetAlpha = AppState.main.random(0.5f, 0.8f) * depthFactor,
            fadeInProgress = 0f,
            lifeTime = -startDelay,
            maxLifeTime = AppState.main.random(25f, 40f),
            gradient = generateGradient(),
            depthFactor = depthFactor,
            noiseOffset = AppState.main.random(0f, 1000f),
            verticalDrift = AppState.main.random(-1f, 1f),
            horizontalDrift = AppState.main.random(-5f, 5f),
            twistAmount = AppState.main.random(0.5f, 2.5f),
            pulseSpeed = AppState.main.random(0.5f, 1.5f),
            pulseAmount = AppState.main.random(0.1f, 0.3f)
        )
    }

    fun physicsUpdate() {
        val deltaTime = 1f / 60f
        time += deltaTime

        waves.forEach { wave ->
            wave.lifeTime += deltaTime

            // Пропускаем обновление если волна еще не началась (задержка старта)
            if (wave.lifeTime < 0f) return@forEach

            // Плавное появление (первые 5 секунд)
            if (wave.fadeInProgress < 1f) {
                wave.fadeInProgress += deltaTime * 0.2f
                wave.fadeInProgress = wave.fadeInProgress.coerceAtMost(1f)
            }

            // Плавное исчезновение (последние 10 секунд)
            val fadeOutStart = wave.maxLifeTime - 10f
            val fadeOutFactor = if (wave.lifeTime > fadeOutStart) {
                val progress = (wave.lifeTime - fadeOutStart) / 10f
                1f - (progress * progress) // Квадратичное затухание для плавности
            } else {
                1f
            }

            // Обновляем фазу
            wave.phase += wave.phaseSpeed * deltaTime

            // Пульсация прозрачности
            val pulse = sin((time * wave.pulseSpeed).toDouble()).toFloat() * wave.pulseAmount
            val baseFade = wave.fadeInProgress * fadeOutFactor
            wave.alpha = (wave.targetAlpha + pulse) * baseFade

            // Медленный дрейф
            wave.baseY += wave.verticalDrift * deltaTime
            wave.centerX += wave.horizontalDrift * deltaTime

            // Пересоздание волны после полного исчезновения
            if (wave.lifeTime >= wave.maxLifeTime) {
                val newWave = generateWave(waves.indexOf(wave))
                wave.centerX = newWave.centerX
                wave.baseY = newWave.baseY
                wave.horizontalSpread = newWave.horizontalSpread
                wave.amplitude = newWave.amplitude
                wave.frequency = newWave.frequency
                wave.phase = newWave.phase
                wave.phaseSpeed = newWave.phaseSpeed
                wave.height = newWave.height
                wave.targetAlpha = newWave.targetAlpha
                wave.fadeInProgress = 0f
                wave.lifeTime = 0f
                wave.maxLifeTime = newWave.maxLifeTime
                wave.gradient = newWave.gradient
                wave.depthFactor = newWave.depthFactor
                wave.noiseOffset = newWave.noiseOffset
                wave.verticalDrift = newWave.verticalDrift
                wave.horizontalDrift = newWave.horizontalDrift
                wave.twistAmount = newWave.twistAmount
                wave.pulseSpeed = newWave.pulseSpeed
                wave.pulseAmount = newWave.pulseAmount
            }
        }
    }

    fun render() {
        waves.forEach { wave ->
            renderWave(wave)
        }
    }

    private fun renderWave(wave: AuroraWave) {
        val segmentWidth = 3f
        val startX = wave.centerX - wave.horizontalSpread
        val endX = wave.centerX + wave.horizontalSpread

        var x = startX
        while (x < endX) {
            val localX = x - wave.centerX

            // Затухание к краям
            val horizontalFade = 1f - (abs(localX) / wave.horizontalSpread).pow(1.5f)

            // Волнообразная форма
            val waveY = sin((localX * wave.frequency + wave.phase).toDouble()).toFloat() * wave.amplitude + 200f

            // Закручивание
            val twist = sin((localX * 0.003f + time * 0.3f).toDouble()).toFloat() * wave.twistAmount * 30f

            // Многослойный шум
            val noise1 = (AppState.main.noise(
                wave.noiseOffset + localX * 0.004f,
                time * 0.15f
            ) - 0.5f) * 40f * wave.depthFactor

            val noise2 = (AppState.main.noise(
                wave.noiseOffset * 2f + localX * 0.008f,
                time * 0.25f + 100f
            ) - 0.5f) * 20f * wave.depthFactor

            val finalY = wave.baseY + waveY + noise1 + noise2 + twist

            // Вариация прозрачности
            val noiseAlpha = AppState.main.noise(
                wave.noiseOffset + localX * 0.006f,
                time * 0.2f
            ).pow(1.5f)

            // Эффект лучей
            val rayEffect = abs(sin((localX * 0.02f + time * 0.5f).toDouble()).toFloat())

            val localAlpha = wave.alpha * horizontalFade * noiseAlpha * (0.6f + rayEffect * 0.4f)

            // Вариация высоты
            val heightNoise1 = AppState.main.noise(
                wave.noiseOffset + localX * 0.01f,
                time * 0.1f
            )
            val heightNoise2 = AppState.main.noise(
                wave.noiseOffset * 3f + localX * 0.005f,
                time * 0.18f + 200f
            )
            val heightVariation = (heightNoise1 * 0.6f + heightNoise2 * 0.4f)
            val localHeight = wave.height * (0.5f + heightVariation * 0.8f)

            // Дрожь
            val jitterX = (AppState.main.noise(
                wave.noiseOffset + localX * 0.05f + time * 2f
            ) - 0.5f) * 2f

            val jitterWidth = segmentWidth * (1.2f +
                    (AppState.main.noise(wave.noiseOffset + localX * 0.03f, time * 0.4f) - 0.5f) * 0.4f)

            // Отрисовка
            if (localAlpha > 0.01f && horizontalFade > 0.1f) {
                parent.lg.setTint(255f, 255f, 255f, 255f * localAlpha)
                parent.lg.setImage(
                    wave.gradient,
                    Vec2(x + jitterX, finalY/2f),
                    Vec2(jitterWidth, localHeight/2f+finalY/3f)
                )
            }

            x += segmentWidth
        }

        parent.lg.noTint()
    }
}