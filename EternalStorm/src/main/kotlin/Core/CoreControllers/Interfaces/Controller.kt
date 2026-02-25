package la.vok.Core.CoreControllers.Intergaces

import la.vok.State.AppState

interface Controller {
    fun create() {
        AppState.logger.info("Controller created [${this.javaClass.simpleName}]")
    }

    fun logicalTick() {

    }
    fun physicTick() {

    }
    fun superTick() {
        AppState.logger.trace("[${this.javaClass.simpleName}] - super.tick()")
    }
}