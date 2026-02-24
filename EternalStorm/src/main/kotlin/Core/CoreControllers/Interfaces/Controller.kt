package la.vok.Core.CoreControllers.Intergaces

import la.vok.State.AppState

interface Controller {
    fun create() {
        AppState.logger.info("Controller created [${this.javaClass.simpleName}]")
    }

    fun tick() {

    }
    fun superTick() {
        AppState.logger.trace("[${this.javaClass.simpleName}] - super.tick()")
    }
}