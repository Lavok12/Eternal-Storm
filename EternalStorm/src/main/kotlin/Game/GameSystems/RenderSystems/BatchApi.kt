package la.vok.Game.GameSystems.RenderSystems

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.State.AppState

class BatchApi(val gameCycle: GameCycle) {
    val gameController: GameController get() = gameCycle.gameController

    val tileBatchSystem = TileBatchSystem(gameController)
    val wallBatchSystem = WallBatchSystem(gameController)
    val aoBatchSystem = AOBatchSystem(gameController)

    private val allSystems = listOf(tileBatchSystem, wallBatchSystem, aoBatchSystem)
    
    var batchesUpdatedThisFrame = 0

    fun tick(camera: Camera, dim: AbstractDimension) {
        if (!AppState.enableBatching) return
        batchesUpdatedThisFrame = 0
        allSystems.forEach { it.tick(camera, dim) }
    }

    fun markDirty(blockX: Int, blockY: Int) {
        if (!AppState.enableBatching) return
        allSystems.forEach { it.markDirty(blockX, blockY) }
    }

    fun markAllDirty() {
        allSystems.forEach { it.markAllDirty() }
    }

    fun clearAll() {
        allSystems.forEach { it.clear() }
    }
}
