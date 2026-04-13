package la.vok.Game.GameController

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.EntitiesList
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi
import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class GameLoader(var gameController: GameController) : Controller {
    val entityApi: EntityApi get() = gameController.gameCycle.entityApi
    val mapApi: MapApi get() = gameController.gameCycle.mapApi
    val itemsApi: ItemsApi get() = gameController.gameCycle.itemsApi
    val gameCycle: GameCycle get() = gameController.gameCycle

    init {
        create()
    }

    fun load() {
        AppState.logger.info("Load Game")

        val objReg = gameController.coreController.objectRegistration
        objReg.dimensions.values.forEach { type ->
            val dimension = type.createDimension(gameCycle)
            gameCycle.dimensionsApi.registerDimension(dimension)
        }

        val mainDim = gameController.gameCycle.dimensionsController.dimensions[DimensionsList.main] ?: return

        entityApi.spawnEntity(mainDim, -1, entityApi.getRegisteredEntity(EntitiesList.player), gameController.mainCamera.pos.copy())
        gameController.playerId = -1

        itemsApi.spawnItemEntity(mainDim, itemsApi.getRegisteredItem(ItemsList.pickaxe, 1), 10 v 80)
        gameController.wGamePanel?.buildInventoryButtons()

        entityApi.spawnEntity(mainDim, EntitiesList.slime, gameController.mainCamera.pos.copy() + (20 v 0))
    }
}