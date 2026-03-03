package la.vok.Game.GameContent.HandItems.Items

import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.VfxObjects.AxeSwingTraceVfxObject
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

class AxeHandItem(item: Item, component: HandItemComponent) : HandItem(
    item,
    component,
    HandItemDescriptor(
        spriteName = "axe.png",
        spriteSize = 5 v 5,
        useDuration = 10f,
        autoRepeat = true,
        animationType = AnimationType.Swing(),
        leftAction = UseAction.Custom(
            onStart = {
                gameCycle.entityApi.damageZone(entity.position + (component.entity.facing*2.2f v 0),
                4.4f v 5,
                DamageData(
                    40,
                    (component.entity.facing*0.2f v 0.15),
                    component.entity.systemId,
                    this),
                TagFilter.HasAll(
                listOf(EntityTags.enemy)
                ))

                gameController.gameCycle.vfxObjectsApi.addInSystem(
                    AxeSwingTraceVfxObject(gameCycle, entity.facing), entity.position + (2f * entity.facing v 0.2f), descriptor.spriteSize * 1.4f,
                    (entity.rigidBody?.speed ?: (Vec2.ZERO)) * (1f v 0.3f)
                )
            },
        ),
    )
)