package Core.CoreControllers

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.State.AppState

class ObjectRegistration(var coreController: CoreController) : Controller
{
    var tiles = HashMap<String, AbstractTileType>()
    var entities = HashMap<String, AbstractEntityType>()
    var items = HashMap<String, AbstractItemType>()


    init {
        create()
    }

    fun clear() {
        tiles.clear()
        entities.clear()
        items.clear()
    }

    fun registrationTileType(tileType: AbstractTileType) {
        AppState.logger.debug("Registration tile type: ${tileType.tag}")
        tiles[tileType.tag] = tileType
    }
    fun registrationEntityType(entityType: AbstractEntityType) {
        AppState.logger.debug("Registration entity type: ${entityType.tag}")
        entities[entityType.tag] = entityType
    }
    fun registrationItemType(itemType: AbstractItemType) {
        AppState.logger.debug("Registration item type: ${itemType.tag}")
        items[itemType.tag] = itemType
    }

}