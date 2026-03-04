package Core.CoreControllers.Loaders

import Core.CoreControllers.ObjectRegistration
import Game.GameContent.TileTypes.GrassTileType
import la.vok.Core.CoreControllers.CoreController
import la.vok.Game.GameContent.Crafts.CraftRegistrator
import la.vok.Game.GameContent.Entities.EntitiTypes.*
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.*
import la.vok.Game.GameContent.Items.ItemTypes.*
import la.vok.Game.GameContent.TileTypes.DirtTileType
import la.vok.Game.GameContent.TileTypes.StoneTileType
import la.vok.Game.GameContent.WallTypes.DirtWallType

class MainContentRegistration(var coreController: CoreController) {
    fun regObjects(objectRegistration: ObjectRegistration) {
        registerItems(objectRegistration)
        registerTiles(objectRegistration)
        registerWalls(objectRegistration)
        registerEntities(objectRegistration)
        registerCrafts(objectRegistration)
    }

    private fun registerItems(objectRegistration: ObjectRegistration) {
        // Tools
        objectRegistration.registrationItemType(AxeItemType())
        objectRegistration.registrationItemType(SpearItemType())
        objectRegistration.registrationItemType(PickaxeItemType())

        // Tile items
        objectRegistration.registrationItemType(GrassTileItemType())
        objectRegistration.registrationItemType(DirtTileItemType())
        objectRegistration.registrationItemType(StoneTileItemType())

        // Wall items
        objectRegistration.registrationItemType(DirtWallItemType())

        // Other
        objectRegistration.registrationItemType(MostCommonStickItemType())
    }

    private fun registerTiles(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationTileType(GrassTileType())
        objectRegistration.registrationTileType(DirtTileType())
        objectRegistration.registrationTileType(StoneTileType())
    }

    private fun registerWalls(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationWallType(DirtWallType())
    }

    private fun registerEntities(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationEntityType(PlayerEntityType())
        objectRegistration.registrationEntityType(SlimeEntityType())
        objectRegistration.registrationEntityType(BossEntityType())
    }

    private fun registerCrafts(objectRegistration: ObjectRegistration) {
        CraftRegistrator.registration(objectRegistration)
        objectRegistration.resolveAllCrafts()
    }
}