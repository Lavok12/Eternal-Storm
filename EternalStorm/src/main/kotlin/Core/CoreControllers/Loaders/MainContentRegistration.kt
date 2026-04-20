package Core.CoreControllers.Loaders

import Core.CoreControllers.ObjectRegistration
import la.vok.Game.GameContent.TileTypes.GrassTileType
import la.vok.Core.CoreControllers.CoreController
import la.vok.Game.GameContent.Crafts.CraftRegistrator
import la.vok.Game.GameContent.Entities.EntitiTypes.*
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.*
import la.vok.Game.GameContent.Items.ItemTypes.Tiles.PlankTileItemType
import la.vok.Game.GameContent.Items.ItemTypes.Walls.PlankWallItemType
import la.vok.Game.GameContent.TileTypes.DirtTileType
import la.vok.Game.GameContent.TileTypes.*
import la.vok.Game.GameContent.WallTypes.*
import la.vok.Game.GameContent.Items.ItemTypes.Tiles.*
import la.vok.Game.GameContent.Items.ItemTypes.Walls.*
import la.vok.Game.GameContent.WallTypes.PlankWallType
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.MainDimensionType
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.BrickWorldDimensionType
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.StoneWorldDimensionType
import la.vok.Game.GameContent.Tiles.System.BigTestBlockType
import la.vok.Game.GameContent.Tiles.System.MultiTileDummyType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Items.ItemTypes.Other.AxeItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.HummerItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.MostCommonStickItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.PickaxeItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.SpearItemType
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.Game.GameContent.Items.ItemTypes.Other.TeleporterItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.*
import la.vok.Game.GameContent.TileTypes.WorkbenchTileType

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

        // Material Pickaxes
        objectRegistration.registrationItemType(WoodenPickaxeItemType())
        objectRegistration.registrationItemType(StonePickaxeItemType())
        objectRegistration.registrationItemType(CopperPickaxeItemType())
        objectRegistration.registrationItemType(TinPickaxeItemType())
        objectRegistration.registrationItemType(BronzePickaxeItemType())
        objectRegistration.registrationItemType(IronPickaxeItemType())
        objectRegistration.registrationItemType(MagicalPickaxeItemType())
        objectRegistration.registrationItemType(CelestialPickaxeItemType())
        objectRegistration.registrationItemType(CosmicPickaxeItemType())
        objectRegistration.registrationItemType(WindPickaxeItemType())
        objectRegistration.registrationItemType(SolarPickaxeItemType())

        // Tile items
        objectRegistration.registrationItemType(GrassTileItemType())
        objectRegistration.registrationItemType(DirtTileItemType())
        objectRegistration.registrationItemType(StoneTileItemType())
        objectRegistration.registrationItemType(GoldOreTileItemType())
        objectRegistration.registrationItemType(DiamondOreTileItemType())
        objectRegistration.registrationItemType(BigTestBlockItemType())
        objectRegistration.registrationItemType(PlankTileItemType())

        // Material Brick Tile Items
        objectRegistration.registrationItemType(WoodenBrickTileItemType())
        objectRegistration.registrationItemType(StoneBrickTileItemType())
        objectRegistration.registrationItemType(CopperBrickTileItemType())
        objectRegistration.registrationItemType(TinBrickTileItemType())
        objectRegistration.registrationItemType(BronzeBrickTileItemType())
        objectRegistration.registrationItemType(IronBrickTileItemType())
        objectRegistration.registrationItemType(MagicalBrickTileItemType())
        objectRegistration.registrationItemType(CelestialBrickTileItemType())
        objectRegistration.registrationItemType(CosmicBrickTileItemType())
        objectRegistration.registrationItemType(WindBrickTileItemType())
        objectRegistration.registrationItemType(SolarBrickTileItemType())

        // Wall items
        objectRegistration.registrationItemType(DirtWallItemType())
        objectRegistration.registrationItemType(PlankWallItemType())

        // Material Brick Wall Items
        objectRegistration.registrationItemType(WoodenBrickWallItemType())
        objectRegistration.registrationItemType(StoneBrickWallItemType())
        objectRegistration.registrationItemType(CopperBrickWallItemType())
        objectRegistration.registrationItemType(TinBrickWallItemType())
        objectRegistration.registrationItemType(BronzeBrickWallItemType())
        objectRegistration.registrationItemType(IronBrickWallItemType())
        objectRegistration.registrationItemType(MagicalBrickWallItemType())
        objectRegistration.registrationItemType(CelestialBrickWallItemType())
        objectRegistration.registrationItemType(CosmicBrickWallItemType())
        objectRegistration.registrationItemType(WindBrickWallItemType())
        objectRegistration.registrationItemType(SolarBrickWallItemType())

        // Other
        objectRegistration.registrationItemType(MostCommonStickItemType())
        objectRegistration.registrationItemType(TeleporterItemType())
        objectRegistration.registrationItemType(WorkbenchItemType())
        objectRegistration.registrationItemType(CzechFlagItemType())
        objectRegistration.registrationItemType(FrameItemType())
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

        // Material Brick Tiles
        objectRegistration.registrationTileType(WoodenBrickTileType())
        objectRegistration.registrationTileType(StoneBrickTileType())
        objectRegistration.registrationTileType(CopperBrickTileType())
        objectRegistration.registrationTileType(TinBrickTileType())
        objectRegistration.registrationTileType(BronzeBrickTileType())
        objectRegistration.registrationTileType(IronBrickTileType())
        objectRegistration.registrationTileType(MagicalBrickTileType())
        objectRegistration.registrationTileType(CelestialBrickTileType())
        objectRegistration.registrationTileType(CosmicBrickTileType())
        objectRegistration.registrationTileType(WindBrickTileType())
        objectRegistration.registrationTileType(SolarBrickTileType())
        objectRegistration.registrationTileType(WorkbenchTileType())
        objectRegistration.registrationTileType(CzechFlagTileType())
        objectRegistration.registrationTileType(FrameTileType())
    }

    private fun registerWalls(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationWallType(DirtWallType())
        objectRegistration.registrationWallType(PlankWallType())

        // Material Brick Walls
        objectRegistration.registrationWallType(WoodenBrickWallType())
        objectRegistration.registrationWallType(StoneBrickWallType())
        objectRegistration.registrationWallType(CopperBrickWallType())
        objectRegistration.registrationWallType(TinBrickWallType())
        objectRegistration.registrationWallType(BronzeBrickWallType())
        objectRegistration.registrationWallType(IronBrickWallType())
        objectRegistration.registrationWallType(MagicalBrickWallType())
        objectRegistration.registrationWallType(CelestialBrickWallType())
        objectRegistration.registrationWallType(CosmicBrickWallType())
        objectRegistration.registrationWallType(WindBrickWallType())
        objectRegistration.registrationWallType(SolarBrickWallType())
    }

    private fun registerEntities(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationEntityType(PlayerEntityType())
        objectRegistration.registrationEntityType(SlimeEntityType())
        objectRegistration.registrationEntityType(BossEntityType())
    }

    private fun registerDimensions(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationDimensionType(MainDimensionType())
        objectRegistration.registrationDimensionType(BrickWorldDimensionType())
        objectRegistration.registrationDimensionType(StoneWorldDimensionType())
    }

    private fun registerCrafts(objectRegistration: ObjectRegistration) {
        CraftRegistrator.registration(objectRegistration)
        objectRegistration.resolveAllCrafts()
    }
}