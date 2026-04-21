package Core.CoreControllers

import la.vok.Core.CoreContent.Resources.ResourceSource
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameContent.Crafts.CraftType
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimensionType
import la.vok.State.AppState
import la.vok.State.AppState.main

class ObjectRegistration(var coreController: CoreController) : Controller {
    var tiles = HashMap<String, AbstractTileType>()
    var walls = HashMap<String, AbstractWallType>()
    var entities = HashMap<String, AbstractEntityType>()
    var items = HashMap<String, AbstractItemType>()
    var dimensions = HashMap<String, AbstractDimensionType>()
    var crafts = ArrayList<CraftType>()
    val craftsByPriority = HashMap<Int, ArrayList<CraftType>>()

    val resourceSources = mutableListOf<ResourceSource>()

    fun initBaseSources() {
        resourceSources.clear()
        resourceSources.add(ResourceSource("eternal_storm", main.dataPath("Content"), 0))
    }


    init { create() }

    fun clear() {
        tiles.clear()
        walls.clear()
        entities.clear()
        items.clear()
        dimensions.clear()
        crafts.clear()
        craftsByPriority.clear()
    }

    fun registrationTileType(tileType: AbstractTileType) {
        AppState.logger.debug("Registration tile type: ${tileType.tag}")
        tiles[tileType.tag] = tileType
    }

    fun registrationWallType(wallType: AbstractWallType) {
        AppState.logger.debug("Registration wall type: ${wallType.tag}")
        walls[wallType.tag] = wallType
    }

    fun registrationEntityType(entityType: AbstractEntityType) {
        AppState.logger.debug("Registration entity type: ${entityType.tag}")
        entities[entityType.tag] = entityType
    }

    fun registrationItemType(itemType: AbstractItemType) {
        AppState.logger.debug("Registration item type: ${itemType.tag}")
        items[itemType.tag] = itemType
    }

    fun registrationDimensionType(dimensionType: AbstractDimensionType) {
        AppState.logger.debug("Registration dimension type: ${dimensionType.tag}")
        dimensions[dimensionType.tag] = dimensionType
    }

    fun registrationCraft(craftType: CraftType) {
        AppState.logger.debug("Registration craft: ${craftType.result.tag} (priority: ${craftType.priority})")
        craftType.resolve(this)
        crafts += craftType
        craftsByPriority.getOrPut(craftType.priority) { ArrayList() } += craftType
    }

    fun resolveAllCrafts() {
        crafts.forEach { it.resolve(this) }
    }

    fun getTileType(tag: String): AbstractTileType =
        tiles[tag] ?: error("TileType not found: $tag")

    fun getWallType(tag: String): AbstractWallType =
        walls[tag] ?: error("WallType not found: $tag")

    fun getEntityType(tag: String): AbstractEntityType =
        entities[tag] ?: error("EntityType not found: $tag")

    fun getItemType(tag: String): AbstractItemType =
        items[tag] ?: error("ItemType not found: $tag")

    fun getDimensionType(tag: String): AbstractDimensionType =
        dimensions[tag] ?: error("DimensionType not found: $tag")
}