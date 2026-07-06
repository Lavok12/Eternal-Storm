package Core.CoreControllers.Loaders

import Core.CoreControllers.ObjectRegistration
import la.vok.Game.GameContent.TileTypes.GrassTileType
import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.FrameLimiter
import la.vok.Game.GameContent.ContentList.LiquidList
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
import la.vok.Game.GameContent.Items.ItemTypes.Hoes.*
import la.vok.Game.GameContent.Items.ItemTypes.Other.WheatItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.WheatSeedsItemType
import la.vok.Game.GameContent.WallTypes.PlankWallType
import la.vok.Game.GameContent.Dimensions.Dimensions.MainDimensionType
import la.vok.Game.GameContent.Dimensions.Dimensions.BrickWorldDimensionType
import la.vok.Game.GameContent.Dimensions.Dimensions.StoneWorldDimensionType
import la.vok.Game.GameContent.Tiles.System.BigTestBlockType
import la.vok.Game.GameContent.Tiles.System.MultiTileDummyType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.HandItems.List.SinGunHandItem
import la.vok.Game.GameContent.Items.ItemTypes.Other.AxeItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.HummerItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.MostCommonStickItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.PickaxeItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.SpearItemType
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.Game.GameContent.Items.ItemTypes.Other.TeleporterItemType
import la.vok.Game.GameContent.Items.ItemTypes.Other.*
import la.vok.Game.GameContent.TileTypes.WorkbenchTileType
import la.vok.Game.GameContent.LiquidTypes.*
import la.vok.Game.GameContent.Items.ItemTypes.Liquid.*
import la.vok.Game.GameContent.Items.Items.SinGunItem
import la.vok.Game.GameSystems.EntityComponents.Buffs.*
import la.vok.Game.GameContent.ContentList.BuffTags
import la.vok.Game.GameContent.ContentList.StatTags
import la.vok.Game.GameContent.CustomBuffTypes.BuffType
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameSystems.EntityComponents.Buffs.BuffRegistry
import la.vok.Game.GameContent.TileData.ChestTileData
import la.vok.Game.GameContent.TileTypes.ChestTileType
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.ChestTileItemType
import la.vok.Game.GameSystems.EntityComponents.HpBody
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.LavokLibrary.Vectors.v

class MainContentRegistration(var coreController: CoreController) {
    fun regObjects(objectRegistration: ObjectRegistration) {
        registerItems(objectRegistration)
        registerTiles(objectRegistration)
        registerWalls(objectRegistration)
        registerEntities(objectRegistration)
        registerDimensions(objectRegistration)
        registerLiquids(objectRegistration)
        registerCrafts(objectRegistration)
        registerBuffs(objectRegistration)
    }

    private fun registerItems(objectRegistration: ObjectRegistration) {
        // Tools
        objectRegistration.registrationItemType(AxeItemType())
        objectRegistration.registrationItemType(SpearItemType())
        objectRegistration.registrationItemType(PickaxeItemType())
        objectRegistration.registrationItemType(HummerItemType())
        objectRegistration.registrationItemType(CopperHoeItemType())

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
        objectRegistration.registrationItemType(SandTileItemType())
        objectRegistration.registrationItemType(SandstoneTileItemType())
        objectRegistration.registrationItemType(StoneTileItemType())
        objectRegistration.registrationItemType(GoldOreTileItemType())
        objectRegistration.registrationItemType(DiamondOreTileItemType())
        objectRegistration.registrationItemType(CopperOreTileItemType())
        objectRegistration.registrationItemType(IronOreTileItemType())
        objectRegistration.registrationItemType(BigTestBlockItemType())
        objectRegistration.registrationItemType(PlankTileItemType())
        objectRegistration.registrationItemType(ChestTileItemType())

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
        objectRegistration.registrationItemType(SandWallItemType())
        objectRegistration.registrationItemType(SandstoneWallItemType())
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
        objectRegistration.registrationItemType(SinGunItemType())

        objectRegistration.registrationItemType(TeleporterItemType())
        objectRegistration.registrationItemType(WorkbenchItemType())
        objectRegistration.registrationItemType(CzechFlagItemType())
        objectRegistration.registrationItemType(FrameItemType())
        objectRegistration.registrationItemType(SmallGrassTileItemType())
        objectRegistration.registrationItemType(SunflowerItemType())
        objectRegistration.registrationItemType(WheatItemType())
        objectRegistration.registrationItemType(WheatSeedsItemType())
        objectRegistration.registrationItemType(RawCopperItemType())

        objectRegistration.registrationItemType(WaterBucketItemType())
        objectRegistration.registrationItemType(LavaBucketItemType())
        objectRegistration.registrationItemType(AcidBucketItemType())
    }

    private fun registerTiles(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationTileType(GrassTileType())
        objectRegistration.registrationTileType(DirtTileType())
        objectRegistration.registrationTileType(SandTileType())
        objectRegistration.registrationTileType(SandstoneTileType())
        objectRegistration.registrationTileType(StoneTileType())
        objectRegistration.registrationTileType(GoldOreTileType())
        objectRegistration.registrationTileType(DiamondOreTileType())
        objectRegistration.registrationTileType(CopperOreTileType())
        objectRegistration.registrationTileType(IronOreTileType())
        objectRegistration.registrationTileType(FarmlandTileType())
        objectRegistration.registrationTileType(WheatTileType())
        objectRegistration.registrationTileType(PlankTileType())
        objectRegistration.registrationTileType(BigTestBlockType())
        objectRegistration.registrationTileType(MultiTileDummyType(TilesList.multi_tile_dummy, LPoint.ZERO))
        objectRegistration.registrationTileType(TreePartType())
        objectRegistration.registrationTileType(ChestTileType())

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
        objectRegistration.registrationTileType(SmallGrassTileType())
        objectRegistration.registrationTileType(SunflowerTileType())
        objectRegistration.registrationTileType(ObsidianTileType())
    }

    private fun registerWalls(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationWallType(DirtWallType())
        objectRegistration.registrationWallType(SandWallType())
        objectRegistration.registrationWallType(SandstoneWallType())
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
        objectRegistration.registrationEntityType(SpiderBossEntityType())
        objectRegistration.registrationEntityType(SpiderLegEntityType())
        objectRegistration.registrationEntityType(TumbleweedEntityType())
        
        objectRegistration.registrationEntityType(AbstractEntityType.ItemEntityType)
        objectRegistration.registrationEntityType(AbstractEntityType.ProjectileEntityType)
        objectRegistration.registrationEntityType(AbstractEntityType.EmptyEntityType)
        objectRegistration.registrationEntityType(AbstractEntityType.FallingBlockEntityType)
        objectRegistration.registrationEntityType(AbstractEntityType.DamageEntityType)
    }

    private fun registerDimensions(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationDimensionType(MainDimensionType())
        objectRegistration.registrationDimensionType(BrickWorldDimensionType())
        objectRegistration.registrationDimensionType(StoneWorldDimensionType())
    }

    private fun registerLiquids(objectRegistration: ObjectRegistration) {
        objectRegistration.registrationLiquidType(WaterLiquidType())
        objectRegistration.registrationLiquidType(LavaLiquidType())
        objectRegistration.registrationLiquidType(AcidLiquidType())

        // Interactions: All pairs result in stone for now
        objectRegistration.registrationLiquidInteraction(
            TileReaction(
                LiquidList.WATER_ID, 
                LiquidList.LAVA_ID, 
                minAmount1 = 50,
                minAmount2 = 50, 
                resultTileTag = TilesList.stone_block
            )
        )
        objectRegistration.registrationLiquidInteraction(
            TileReaction(
                LiquidList.WATER_ID,
                LiquidList.ACID_ID, 
                minAmount1 = 50,
                minAmount2 = 50, 
                resultTileTag = TilesList.stone_block
            )
        )
        objectRegistration.registrationLiquidInteraction(
            TileReaction(
                LiquidList.LAVA_ID, 
                LiquidList.ACID_ID, 
                minAmount1 = 50,
                minAmount2 = 50, 
                resultTileTag = TilesList.stone_block
            )
        )
    }

    private fun registerBuffs(objectRegistration: ObjectRegistration) {
        // Speed Buff: +20% movement speed (Multiply)
        BuffRegistry.register(BuffType(
            tag = BuffTags.SPEED,
            maxTicks = 60 * 10, // 10 seconds at 60fps
            modifiers = listOf(
                Modifier(StatTags.SPEED, 1.2f, ModifierType.MULTIPLY)
            )
        ))

        // Rage Buff: +50% damage, -10% resistance (Multiply)
        BuffRegistry.register(BuffType(
            tag = BuffTags.RAGE,
            maxTicks = 60 * 5,
            modifiers = listOf(
                Modifier(StatTags.DAMAGE, 1.5f, ModifierType.MULTIPLY),
                Modifier(StatTags.RESISTANCE, 0.9f, ModifierType.MULTIPLY)
            )
        ))

        // Regeneration Buff: +5 fixed regen (Add)
        BuffRegistry.register(BuffType(
            tag = BuffTags.REGENERATION,
            maxTicks = 60 * 15,
            modifiers = listOf(
                Modifier(StatTags.REGEN, 5f, ModifierType.ADD)
            )
        ))

        // Water touch status effect (wet flag)
        BuffRegistry.register(BuffType(
            tag = BuffTags.WET,
            maxTicks = -1,
            modifiers = listOf(
                Modifier(BuffTags.WET, 1f, ModifierType.FLAG)
            )
        ))

        // Lava touch status effect (burning flag)
        BuffRegistry.register(BuffType(
            tag = BuffTags.BURNING,
            maxTicks = -1,
            modifiers = listOf(
                Modifier(BuffTags.BURNING, 1f, ModifierType.FLAG)
            ),
            onTick = {entity ->
                if (FrameLimiter.totalPhysicsFrames % 60f == 0f) {
                    entity.gameCycle.entityApi.absoluteDamage(
                        entity.dimension,
                        entity,
                        DamageData(25, 0 v 0.1, -1, null, false)
                    )
                }
            }
        ))

        // Acid touch status effect (acid burn flag)
        BuffRegistry.register(BuffType(
            tag = BuffTags.ACID_BURN,
            maxTicks = -1,
            modifiers = listOf(
                Modifier(BuffTags.ACID_BURN, 1f, ModifierType.FLAG)
            ),
            onTick = {entity ->
                entity.gameCycle.entityApi.absoluteDamage(entity.dimension, entity, DamageData(1, 0 v 0, -1, null))
            }
        ))


        // Drowning status effect (ticks-based drowning damage)
        BuffRegistry.register(BuffType(
            tag = BuffTags.DROWNING,
            maxTicks = -1,
            onTick = { entity ->
                val activeBuff = entity.buffController.getActiveBuffs().find { it.type.tag == BuffTags.DROWNING }
                val ticks = activeBuff?.tickCount ?: 0
                val oxygenComp = entity.getComponent<la.vok.Game.GameSystems.EntityComponents.OxygenComponent>()
                val interval = oxygenComp?.drownDamageInterval ?: 60
                
                if (ticks > 0 && ticks % interval == 0) {
                    val damage = oxygenComp?.drownDamage ?: 10
                    val hitbox = entity.mainHitbox ?: entity.hitboxes.values.firstOrNull()
                    if (hitbox != null) {
                        entity.takeDamage(la.vok.Game.GameSystems.WorldSystems.Entities.DamageData(damage, la.vok.LavokLibrary.Vectors.Vec2.ZERO, null, null), hitbox)
                    }
                }
            }
        ))
    }

    private fun registerCrafts(objectRegistration: ObjectRegistration) {
        CraftRegistrator.registration(objectRegistration)
        objectRegistration.resolveAllCrafts()
    }
}