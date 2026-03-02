package la.vok.Game.GameContent.HandItems.Weapons

import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.v

class TileHandItem(item: Item, component: HandItemComponent, var abstractTileType: AbstractTileType) : HandItem(
    item,
    component,
    HandItemDescriptor(
        spriteName = abstractTileType.texture,
        spriteSize = 1 v 1,
        useDuration = 6f,
        autoRepeat = true,
        renderPlaceHighlight = true,
        animationType = AnimationType.Idle,
        leftAction = UseAction.Custom(
            onStart = {
                var placePos = handItemComponent.targetMapPos()
                gameCycle.mapApi.controlPlaceTile(abstractTileType, placePos.x, placePos.y, item, abstractTileType.consumed)
            },
        ),
    )
)