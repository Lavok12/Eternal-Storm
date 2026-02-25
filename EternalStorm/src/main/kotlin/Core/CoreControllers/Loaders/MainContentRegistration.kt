package Core.CoreControllers.Loaders

import Core.CoreControllers.ObjectRegistration
import Game.GameContent.TileTypes.GrassTileType
import la.vok.Core.CoreControllers.CoreController
import la.vok.Game.GameContent.Entities.EntitiTypes.TestEntityType
import la.vok.Game.GameContent.Entities.Entities.TestEntity
import la.vok.Game.GameContent.Tiles.TileTypes.DirtTileType
import la.vok.Game.GameContent.Tiles.TileTypes.StoneTileType
import la.vok.State.AppState

class MainContentRegistration(coreController: CoreController) {
    fun regObjects(objectRegistration: ObjectRegistration) {
        AppState.logger.debug("RegisterTiles")
        objectRegistration.registrationTileType(StoneTileType())
        objectRegistration.registrationTileType(GrassTileType())
        objectRegistration.registrationTileType(DirtTileType())

        objectRegistration.registrationEntityType(TestEntityType())
    }
}