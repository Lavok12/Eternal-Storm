package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.FrameLimiter
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

enum class UpdatePhase {
    PRE, MAIN, POST
}

class UpdateController(val gameCycle: GameCycle) : Controller {
    private var lastSecondUpdate: Long = FrameLimiter.uptimeMs
    private var lastMinuteUpdate: Long = FrameLimiter.uptimeMs

    override fun logicalTick() {
        val now = FrameLimiter.uptimeMs

        // --- Logical Tick Updates ---
        gameCycle.mapApi.dispatchLogicalUpdate(UpdatePhase.PRE)
        gameCycle.mapApi.dispatchLogicalUpdate(UpdatePhase.MAIN)
        gameCycle.mapApi.dispatchLogicalUpdate(UpdatePhase.POST)

        if (now - lastSecondUpdate >= 1000) {
            lastSecondUpdate = now
            
            // --- Second Updates ---
            gameCycle.mapApi.dispatchSecondUpdate(UpdatePhase.PRE)
            gameCycle.mapApi.dispatchSecondUpdate(UpdatePhase.MAIN)
            gameCycle.mapApi.dispatchSecondUpdate(UpdatePhase.POST)

            if (now - lastMinuteUpdate >= 60000) {
                lastMinuteUpdate = now
                
                // --- Minute Updates ---
                gameCycle.mapApi.dispatchMinuteUpdate(UpdatePhase.PRE)
                gameCycle.mapApi.dispatchMinuteUpdate(UpdatePhase.MAIN)
                gameCycle.mapApi.dispatchMinuteUpdate(UpdatePhase.POST)
            }
        }
    }

    override fun renderTick() {
        gameCycle.mapApi.dispatchRenderUpdate(UpdatePhase.PRE)
        gameCycle.mapApi.dispatchRenderUpdate(UpdatePhase.MAIN)
        gameCycle.mapApi.dispatchRenderUpdate(UpdatePhase.POST)
    }

    override fun physicTick() {
        gameCycle.mapApi.dispatchPhysicsUpdate(UpdatePhase.PRE)
        gameCycle.mapApi.dispatchPhysicsUpdate(UpdatePhase.MAIN)
        gameCycle.mapApi.dispatchPhysicsUpdate(UpdatePhase.POST)
    }

    /**
     * Call detailed updates for a specific dimension.
     */
    fun updateDimension(dimension: AbstractDimension) {
        dimension.mapSystem.dispatchLogicalUpdate(UpdatePhase.PRE)
        dimension.mapSystem.dispatchLogicalUpdate(UpdatePhase.MAIN)
        dimension.mapSystem.dispatchLogicalUpdate(UpdatePhase.POST)
        dimension.mapSystem.dispatchPhysicsUpdate(UpdatePhase.PRE)
        dimension.mapSystem.dispatchPhysicsUpdate(UpdatePhase.MAIN)
        dimension.mapSystem.dispatchPhysicsUpdate(UpdatePhase.POST)
    }

    fun renderDimension(dimension: AbstractDimension, phase: UpdatePhase) {
        dimension.mapSystem.dispatchRenderUpdate(phase)
    }
}
