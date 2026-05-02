package la.vok.Game.GameContent.HandItems.List

import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.SpeedMultiplierType
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameContent.Items.Items.Hoes.HoeItem
import la.vok.Game.GameContent.VfxObjects.AxeSwingTraceVfxObject
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class HoeHandItem(item: HoeItem, component: HandItemComponent) : HandItem(
    item,
    component,
    HandItemDescriptor(
        speedType = SpeedMultiplierType.DiggingWall,
        spriteName = item.handItemTexture,
        spriteSize = item.handItemSize v item.handItemSize,
        useDuration = item.handItemUseDuration,
        autoRepeat = true,
        animationType = AnimationType.Swing(),
        renderMineHighlight = true,
        leftAction = UseAction.Custom(
            onStart = {
                gameCycle.entityApi.damageZone(
                    entity.dimension!!,
                    entity.position + (component.entity.facing * 2f v 0),
                    (1.1 v 1.5) * item.handItemSize,
                    DamageData(
                        item.handItemDamage,
                        ((entity.facing v 1f) * item.handItemKnockback),
                        component.entity.systemId,
                        this
                    ),
                    TagFilter.HasAll(
                        listOf(EntityTags.enemy)
                    )
                )

                gameController.gameCycle.vfxObjectsApi.addInSystem(
                    entity.dimension!!,
                    AxeSwingTraceVfxObject(gameCycle, entity.facing),
                    entity.position + ((0.5f+descriptor.spriteSize.x/2f) * entity.facing v 0.2f),
                    descriptor.spriteSize * 1.4f,
                    (entity.rigidBody?.speed ?: (Vec2.ZERO)) * (1f v 0.3f)
                )

                (this as HoeHandItem).onPlowTile()
            },
        ),
    )
) {
    open fun onPlowTile() {
        val placePos = entity.ai?.targetMapPos() ?: LPoint.ZERO
        val dimension = entity.dimension ?: return
        val mapApi = gameCycle.mapApi
        
        val targetTileType = mapApi.getTileType(dimension, placePos.x, placePos.y) ?: return
        
        if (targetTileType.hasTag(BlockTags.PLOWABLE)) {
            // Check if there is no block above
            if (!mapApi.tileIsActive(dimension, placePos.x, placePos.y + 1)) {
                val farmlandType = gameController.coreController.objectRegistration.tiles[TilesList.farmland_block]
                dimension.mapSystem.setTileType(placePos.x, placePos.y, farmlandType)
                dimension.mapSystem.setTileHp(placePos.x, placePos.y, farmlandType?.maxHp ?: 10)
                mapApi.updateNeighbors(dimension, placePos.x, placePos.y)
            }
        } else if (targetTileType.hasTag(BlockTags.PLANT)) {
            val mineData = la.vok.Game.GameSystems.WorldSystems.Map.MineData(
                value = (item as HoeItem).handItemDamage,
                power = 10,
                sourceId = entity.systemId,
                instrument = this,
                item = item
            )
            mapApi.mineTile(dimension, placePos.x, placePos.y, mineData)
        }
    }
}
