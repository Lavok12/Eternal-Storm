package la.vok.Game.GameContent.HandItems.List

import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.SpeedMultiplierType
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.v

class SeedHandItem(item: Item, component: HandItemComponent, var plantTileType: AbstractTileType, texture: String) : HandItem(
    item,
    component,
    HandItemDescriptor(
        speedType = SpeedMultiplierType.PlacingTile,
        spriteName = texture,
        spriteSize = 1 v 1,
        useDuration = 6f,
        autoRepeat = true,
        renderPlaceHighlight = true,
        animationType = AnimationType.Idle,
        leftAction = UseAction.Custom(
            onStart = {
                val ai = entity.ai ?: return@Custom
                var placePos = ai.targetMapPos()
                val dimension = entity.dimension ?: return@Custom
                val mapApi = gameCycle.mapApi
                
                // If targeted block is Farmland, auto-adjust to place ON TOP
                val targetType = mapApi.getTileType(dimension, placePos.x, placePos.y)
                if (targetType?.tag == TilesList.farmland_block) {
                    placePos = LPoint(placePos.x, placePos.y + 1)
                }

                mapApi.controlPlaceTile(dimension, plantTileType, placePos.x, placePos.y, item, entity.systemId, item.consumed)
            },
        ),
    )
)
