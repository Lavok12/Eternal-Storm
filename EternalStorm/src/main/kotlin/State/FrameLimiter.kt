package la.vok.Core

import la.vok.State.AppState
import java.awt.GraphicsEnvironment
import java.lang.System.nanoTime

object FrameLimiter {

    // ===== DISPLAY =====

    val displayFPS: Int = detectDisplayFPS()

    var targetUpdateFPS: Float = Float.MAX_VALUE
        set(value) {
            field = value
            AppState.main.frameRate(value)
        }

    // ===== TIME =====

    private val startTimeNs: Long = nanoTime()

    val uptimeMs: Long
        get() = (nanoTime() - startTimeNs) / 1_000_000

    var renderDeltaTime = 0f
        private set

    var physicsDeltaTime = 0f
        private set

    // ===== RENDER =====

    private var lastRenderTimeNs: Long = nanoTime()
    private val renderIntervalNs: Long
        get() = 1_000_000_000L / displayFPS

    private var renderFrameCount = 0
    private var lastRenderFpsUpdateTimeNs: Long = nanoTime()
    var currentRenderFPS: Int = 0
        private set

    var totalRenderFrames: Long = 0
        private set

    // ===== PHYSICS =====

    var physicsFPS: Int = 60
        set(value) {
            field = value
            physicsIntervalNs = 1_000_000_000L / value
        }

    private var physicsIntervalNs: Long = 1_000_000_000L / 60
    private var lastPhysicsTimeNs: Long = nanoTime()

    private var physicsFrameCount = 0
    private var lastPhysicsFpsUpdateTimeNs: Long = nanoTime()
    var currentPhysicsFPS: Int = 0
        private set

    var totalPhysicsFrames: Long = 0
        private set

    // ===== UPDATE =====

    val currentUpdateFPS: Int
        get() = AppState.main.frameRate.toInt()

    var totalUpdateFrames: Long = 0
        private set

    // ====================

    fun shouldRender(): Boolean {
        totalUpdateFrames++

        val now = nanoTime()
        var delta = now - lastRenderTimeNs

        if (delta >= renderIntervalNs) {
            if (delta > renderIntervalNs * 2f) delta = renderIntervalNs * 2
            lastRenderTimeNs = now - delta + renderIntervalNs

            renderDeltaTime = delta / 1_000_000_000f

            renderFrameCount++
            totalRenderFrames++

            if (now - lastRenderFpsUpdateTimeNs >= 1_000_000_000L) {
                currentRenderFPS = renderFrameCount
                renderFrameCount = 0
                lastRenderFpsUpdateTimeNs = now
            }
            return true
        }
        return false
    }

    fun shouldUpdatePhysics(): Boolean {
        val now = nanoTime()
        val delta = now - lastPhysicsTimeNs

        if (delta >= physicsIntervalNs) {
            lastPhysicsTimeNs += physicsIntervalNs

            physicsDeltaTime = physicsIntervalNs / 1_000_000_000f

            physicsFrameCount++
            totalPhysicsFrames++

            if (now - lastPhysicsFpsUpdateTimeNs >= 1_000_000_000L) {
                currentPhysicsFPS = physicsFrameCount
                physicsFrameCount = 0
                lastPhysicsFpsUpdateTimeNs = now
            }
            return true
        }
        return false
    }

    fun canRenderNow(): Boolean {
        val now = nanoTime()
        return now - lastRenderTimeNs >= renderIntervalNs
    }

    fun canUpdatePhysicsNow(): Boolean {
        val now = nanoTime()
        return now - lastPhysicsTimeNs >= physicsIntervalNs
    }

    // ===== UTILS =====

    private fun detectDisplayFPS(): Int {
        val gd = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .defaultScreenDevice
        return gd.displayMode.refreshRate.takeIf { it > 0 } ?: 60
    }
}
