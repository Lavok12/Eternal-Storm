package Core.CoreControllers.Loaders

import Core.CoreControllers.ObjectRegistration
import Game.GameContent.TileTypes.GrassTileType
import la.vok.Core.CoreControllers.CoreController
import la.vok.Game.GameContent.Crafts.CraftRegistrator
import la.vok.Game.GameContent.Entities.EntitiTypes.*
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.*
import la.vok.Game.GameContent.Items.ItemTypes.*
import la.vok.Game.GameContent.Items.ItemTypes.Tiles.PlankTileItemType
import la.vok.Game.GameContent.Items.ItemTypes.Walls.PlankWallItemType
import la.vok.Game.GameContent.TileTypes.DirtTileType
import la.vok.Game.GameContent.TileTypes.*
import la.vok.Game.GameContent.TileTypes.StoneTileType
import la.vok.Game.GameContent.WallTypes.*
import la.vok.Game.GameContent.WallTypes.PlankWallType
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.MainDimensionType
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimensionType
import la.vok.Game.GameContent.Tiles.System.BigTestBlockType
import la.vok.Game.GameContent.Tiles.System.MultiTileDummyType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.StoneWorldDimensionType
import la.vok.Game.GameContent.Items.ItemTypes.TeleporterItemType

class MainContentRegistration(var coreController: CoreController) {
    fun regObjects(objectRegistration: ObjectRegistration) {
        registerItems(objectRegistration)
        registerTiles(objectRegistration)
        registerWalls(objectRegistration)
        registerEntities(objectRegistration)
        registerDimensions(objectRegistration)
        registerCrafts(objectRegistration)
    }

    private fun registerItems(objectRegistration: ObjectRegistration) {
        // Tools
        objectRegistration.registrationItemType(AxeItemType())
        objectRegistration.registrationItemType(SpearItemType())
        objectRegistration.registrationItemType(PickaxeItemType())
        objectRegistration.registrationItemType(HummerItemType())

        // Tile items
        objectRegistration.registrationItemType(GrassTileItemType())
        objectRegistration.registrationItemType(DirtTileItemType())
        objectRegistration.registrationItemType(StoneTileItemType())
        objectRegistration.registrationItemType(GoldOreTileItemType())
        objectRegistration.registrationItemType(DiamondOreTileItemType())
        objectRegistration.registrationItemType(BigTestBlockItemType())
        objectRegistration.registrationItemType(PlankTileItemType())

        // Wall items
        objectRegistration.registrationItemType(DirtWallItemType())
        objectRegistration.registrationItemType(PlankWallItemType())

        // Other
        objectRegistration.registrationItemType(MostCommonStickItemType())
        objectRegistration.registrationItemType(TeleporterItemType())
    }

    private fun registerTiles(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationTileType(GrassTileType())
        objectRegistration.registrationTileType(DirtTileType())
        objectRegistration.registrationTileType(StoneTileType())
        objectRegistration.registrationTileType(GoldOreTileType())
        objectRegistration.registrationTileType(DiamondOreTileType())
        objectRegistration.registrationTileType(PlankTileType())
        objectRegistration.registrationTileType(BigTestBlockType())
        objectRegistration.registrationTileType(MultiTileDummyType(TilesList.multi_tile_dummy, LPoint.ZERO))
        objectRegistration.registrationTileType(TreePartType())
    }

    private fun registerWalls(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationWallType(DirtWallType())
        objectRegistration.registrationWallType(PlankWallType())
    }

    private fun registerEntities(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationEntityType(PlayerEntityType())
        objectRegistration.registrationEntityType(SlimeEntityType())
        objectRegistration.registrationEntityType(BossEntityType())
    }

    private fun registerDimensions(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationDimensionType(MainDimensionType())
        objectRegistration.registrationDimensionType(StoneWorldDimensionType())
    }

    private fun registerCrafts(objectRegistration: ObjectRegistration) {
        CraftRegistrator.registration(objectRegistration)
        objectRegistration.resolveAllCrafts()
    }
}