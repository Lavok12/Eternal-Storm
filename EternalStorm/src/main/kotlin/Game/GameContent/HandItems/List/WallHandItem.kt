package la.vok.Game.GameContent.HandItems.List

import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.SpeedMultiplierType
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.v

class WallHandItem(item: Item, component: HandItemComponent, var abstractWallType: AbstractWallType) : HandItem(
    item,
    component,
    HandItemDescriptor(
        speedType = SpeedMultiplierType.PlacingWall,
        spriteName = abstractWallType.texture,
        spriteSize = 1 v 1,
        useDuration = 2f,
        autoRepeat = true,
        renderPlaceHighlight = true,
        animationType = AnimationType.Idle,
        leftAction = UseAction.Custom(
            onStart = {
                var placePos = entity.ai?.targetMapPos() ?: LPoint.Companion.ZERO
                gameCycle.mapApi.controlPlaceWall(entity.dimension!!, abstractWallType, placePos.x, placePos.y, item, item.consumed)
            },
        ),
    )
)